package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p JOIN FETCH p.user u JOIN FETCH p.tagId t")
    Page<Post> findAllWithDetails(Pageable pageable);

    //    @Query("SELECT p, COUNT(pl) AS likeCount, COUNT(c) AS commentCount " +
//            "FROM Post p LEFT JOIN PostLike pl ON pl.post.id = p.id " +
//            "LEFT JOIN Comment c ON c.post.id = p.id " +
//            "GROUP BY p.id")
//    @Query("""
//                select distinct p, count(pl) as likeCount, count(c) as commentCount, u.username, t.tagName, p.thumbnail, u.avatar
//                from Post p
//                left join PostLike pl on pl.post.id = p.id
//                left join Comment c on c.postId = p.id
//                left join User u on u.id = p.user.id
//                left join Tag t on t.id = p.tagId
//                group by p.id
    @Query("""
                SELECT 
                           p,
                           COUNT(DISTINCT l.id), 
                           COUNT(DISTINCT c.id), 
                           u.username, 
                           t.tagName,
                           p.thumbnail,
                           u.avatar,
                           p.id
            
                FROM Post p
                LEFT JOIN Tag t ON p.tagId = t.id
                LEFT JOIN Comment c ON c.postId = p.id
                LEFT JOIN PostLike l ON l.post.id = p.id
                LEFT JOIN User u ON p.user.id = u.id
                GROUP BY p.id, p.title, p.createdAt, p.content, t.tagName, u.avatar, p.thumbnail
            """)
    Page<Object[]> findAllWithLikesAndComments(Pageable pageable);

//            """)

    @Query("""
                select p.title, p.content, t.tagName, count(distinct pl.id), count(distinct c), u.username, p.createdAt, p.updatedAt, u.avatar, p.thumbnail
                from Post p
                left join PostLike pl on pl.post.id = p.id
                left join Comment c on c.postId = p.id
                left join User u on u.id = p.user.id
                left join Tag t on t.id = p.tagId
                where p.id = :postId
            """)
    Object findAllWithLikeAndCommentsByPostId(Integer postId);

    @Query("""
                select p, count(pl) as likeCount, count(c) as commentCount, u.username, t.tagName, p.thumbnail, u.avatar
                from Post p
                left join PostLike pl on pl.post.id = p.id
                left join Comment c on c.postId = p.id
                left join User u on u.id = p.user.id
                left join Tag t on t.id = p.tagId
                where pl.user.id = :userId
                group by p.id
            """)
    Page<Object[]> findPostLikedByUserId(Integer userId, Pageable pageable);

    @Query("""
                select p, count(pl) as likeCount, count(c) as commentCount, u.username, t.tagName, p.thumbnail, u.avatar
                from Post p
                left join PostLike pl on pl.post.id = p.id
                left join Comment c on c.postId = p.id
                left join User u on u.id = p.user.id
                left join Tag t on t.id = p.tagId
                where p.user.id = :userId
                group by p.id
            """)
    Page<Object[]> findPostCreateByUserId(Integer userId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Post p WHERE p.id = :postId")
    void deletePostById(Integer postId);

    @Modifying
    @Query("DELETE FROM Post p WHERE p.id = :postId AND p.user.id = :id")
    void deletePostByIdAndUserId(Integer postId, Integer id);

    Optional<Post> findByIdAndUserId(Integer id, Integer id1);
}