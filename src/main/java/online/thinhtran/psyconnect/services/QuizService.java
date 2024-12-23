package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.quiz.QuizAnswerDto;
import online.thinhtran.psyconnect.entities.Quiz;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.entities.UserAnswer;
import online.thinhtran.psyconnect.repositories.QuizRepository;
import online.thinhtran.psyconnect.repositories.UserAnswerRepository;
import online.thinhtran.psyconnect.responses.quiz.QuizResponse;
import online.thinhtran.psyconnect.responses.quiz.QuizResultResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserAnswerRepository userAnswerRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "quiz")
    public List<QuizResponse> getQuiz() {
        List<Quiz> quizzes = quizRepository.findAll();
        List<QuizResponse> quizResponses = new ArrayList<>();

        quizzes.forEach(quiz -> {
            List<String> choices = Stream.of(
                            quiz.getOption1(),
                            quiz.getOption2(),
                            quiz.getOption3(),
                            quiz.getOption4(),
                            quiz.getOption5(),
                            quiz.getOption6(),
                            quiz.getOption7(),
                            quiz.getOption8()
                    )
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            QuizResponse response = QuizResponse.builder()
                    .id(quiz.getId())
                    .question(quiz.getQuestion())
                    .choice(choices)
                    .build();
            quizResponses.add(response);
        });

        return quizResponses;
    }


    @Transactional
    public void saveAnswer(List<QuizAnswerDto> answerList, Integer userId) {
        List<UserAnswer> userAnswers = answerList.stream()
                .map(answer -> UserAnswer.builder()
                        .userId(userId)
                        .quizId(answer.getQuizId())
                        .answer(answer.getAnswer())
                        .build())
                .toList();

        userAnswerRepository.saveAll(userAnswers);
    }

    @Transactional(readOnly = true)
    public List<QuizResultResponse> getQuizResult(User userId) {
        return userAnswerRepository.getAnswerByUserId(userId.getId());
    }

    @Transactional
    public void deleteAllUserAnswersByUserId(User userId) {
        userAnswerRepository.deleteAllByUserId(userId.getId());
    }
}
