package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    long countByPost_Id(Integer postId);
}