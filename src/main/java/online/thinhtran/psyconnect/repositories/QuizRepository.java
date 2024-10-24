package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}