package online.thinhtran.psyconnect.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.dto.quiz.QuizAnswerDto;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.ai.AIResponse;
import online.thinhtran.psyconnect.responses.quiz.QuizResponse;
import online.thinhtran.psyconnect.responses.quiz.QuizResultResponse;
import online.thinhtran.psyconnect.services.GeminiService;
import online.thinhtran.psyconnect.services.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base-path}/quizzes")
@RequiredArgsConstructor
@Slf4j
public class QuizController {
    private final QuizService quizService;
    private final String result;
    private final GeminiService geminiService;

    @GetMapping
    public ResponseEntity<Response<List<QuizResponse>>> getQuiz() {
        return ResponseEntity.ok(Response.<List<QuizResponse>>builder()
                .data(quizService.getQuiz())
                .message("Quiz fetched successfully")
                .build()
        );
    }

    @PostMapping("/submit")
    public ResponseEntity<Response<AIResponse>> answerQuiz(
            @RequestBody List<QuizAnswerDto> answerDto,
            @AuthenticationPrincipal User user
    ) {
        quizService.deleteAllUserAnswersByUserId(user);

        quizService.saveAnswer(answerDto, user.getId());

        StringBuilder answer = new StringBuilder();
        List<QuizResultResponse> quizResultResponses = quizService.getQuizResult(user);

        for (QuizResultResponse quizResultResponse : quizResultResponses) {
            answer.append(quizResultResponse.toString());
        }

        return ResponseEntity.ok(Response.<AIResponse>builder()
                .data(geminiService.callGemini(String.valueOf(answer)))
                .message("Quiz submitted successfully")
                .build()
        );
    }

    @GetMapping("/result")
    public ResponseEntity<Response<List<QuizResultResponse>>> getQuizResult(
            @AuthenticationPrincipal User user
    ) {
        log.info("UserId from QuizController: {}", user.getId());

        StringBuffer answer = new StringBuffer();
        List<QuizResultResponse> quizResultResponses = quizService.getQuizResult(user);

        for (QuizResultResponse quizResultResponse : quizResultResponses) {
            answer.append(quizResultResponse.toString());
        }

        System.out.println(answer);

        return ResponseEntity.ok(Response.<List<QuizResultResponse>>builder()
                .data(quizService.getQuizResult(user))
                .message("Quiz result fetched successfully")
                .build()
        );
    }
}
