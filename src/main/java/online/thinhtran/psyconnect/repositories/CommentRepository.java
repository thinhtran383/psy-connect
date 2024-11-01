package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Comment;
import online.thinhtran.psyconnect.responses.comments.UserCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    long countByPost_Id(Integer postId);

    @Query("""
         select new online.thinhtran.psyconnect.responses.comments.UserCommentResponse(c.user.username,c.content)
         from Comment c
         join User u on c.user.id = u.id
         where c.post.id = :postId
""")
    Page<UserCommentResponse> findAllByPostId(Integer postId, Pageable pageable);
}