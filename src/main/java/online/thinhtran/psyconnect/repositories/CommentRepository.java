package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Comment;
import online.thinhtran.psyconnect.responses.comments.UserCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    long countByPostId(Integer postId);

    @Query("""
                     select new online.thinhtran.psyconnect.responses.comments.UserCommentResponse(COALESCE(d.name, p.name),u.username,c.content)
                     from Comment c
                     join User u on c.userId = u.id
                     left join Doctor d on u.id = d.user.id
                     left join Patient p on u.id = p.user.id
                     where c.postId = :postId
            """)
    Page<UserCommentResponse> findAllByPostId(Integer postId, Pageable pageable);


    @Modifying
    @Query("DELETE FROM Comment c WHERE c.postId = :postId")
    void deleteCommentsByPostId(@Param("postId") Integer postId);

}