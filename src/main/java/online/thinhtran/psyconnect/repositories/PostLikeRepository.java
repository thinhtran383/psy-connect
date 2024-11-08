package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {
    long countByPost_Id(Integer postId);

    boolean existsByPost_IdAndUser_Id(Integer postId, Integer userId);

    void deleteByPost_IdAndUser_Id(Integer postId, Integer userId);

    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.post.id = :postId")
    void deleteByPostId(Integer postId);
}