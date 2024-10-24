package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
}