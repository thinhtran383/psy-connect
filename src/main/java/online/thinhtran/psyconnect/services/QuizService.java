package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.Quiz;
import online.thinhtran.psyconnect.repositories.QuizRepository;
import online.thinhtran.psyconnect.responses.quiz.QuizResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    @Transactional
    @Cacheable(value = "quiz")
    public List<QuizResponse> getQuiz() {
        List<Quiz> quizzes = quizRepository.findAll();

        return quizzes.stream()
                .map(quiz -> QuizResponse.builder()
                        .id(quiz.getId())
                        .question(quiz.getQuestion())
                        .choice(Arrays.asList(quiz.getOption1(), quiz.getOption2(), quiz.getOption3(), quiz.getOption4()))
                        .build())
                .toList();

    }
}
