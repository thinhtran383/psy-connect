package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.UserAnswer;
import online.thinhtran.psyconnect.responses.quiz.QuizResultResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
    @Query("""
            select new online.thinhtran.psyconnect.responses.quiz.QuizResultResponse(q.question, ua.answer)
            from UserAnswer ua
            join Quiz q on ua.quizId = q.id
            where ua.userId = :userId
            """)
    List<QuizResultResponse> getAnswerByUserId(Integer userId);

    void deleteAllByUserId(Integer id);
}