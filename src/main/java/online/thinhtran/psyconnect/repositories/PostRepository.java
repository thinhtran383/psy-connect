package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}