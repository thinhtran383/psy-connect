package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.quiz.QuizResponse;
import online.thinhtran.psyconnect.services.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.base-path}/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @GetMapping
    public ResponseEntity<Response<List<QuizResponse>>> getQuiz() {
        return ResponseEntity.ok(Response.<List<QuizResponse>>builder()
                .data(quizService.getQuiz())
                .message("Quiz fetched successfully")
                .build()
        );
    }
}
