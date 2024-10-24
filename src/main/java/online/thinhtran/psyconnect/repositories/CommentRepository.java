package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}