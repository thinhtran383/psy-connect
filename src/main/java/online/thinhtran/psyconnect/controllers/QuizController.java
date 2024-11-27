package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.quiz.QuizAnswerDto;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.ai.AIResponse;
import online.thinhtran.psyconnect.responses.quiz.QuizResponse;
import online.thinhtran.psyconnect.services.GeminiService;
import online.thinhtran.psyconnect.services.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base-path}/quizzes")
@RequiredArgsConstructor
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
        quizService.saveAnswer(answerDto, user.getId());

        return ResponseEntity.ok(Response.<AIResponse>builder()
                .data(geminiService.callGemini(result))
                .message("Quiz submitted successfully")
                .build()
        );
    }
}
