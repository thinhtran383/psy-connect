package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p JOIN FETCH p.user u JOIN FETCH p.tag t")
    Page<Post> findAllWithDetails(Pageable pageable);

//    @Query("SELECT p, COUNT(pl) AS likeCount, COUNT(c) AS commentCount " +
//            "FROM Post p LEFT JOIN PostLike pl ON pl.post.id = p.id " +
//            "LEFT JOIN Comment c ON c.post.id = p.id " +
//            "GROUP BY p.id")
    @Query("""
                select p, count(pl) as likeCount, count(c) as commentCount, u.username, t.tagName
                from Post p
                left join PostLike pl on pl.post.id = p.id
                left join Comment c on c.post.id = p.id
                left join User u on u.id = p.user.id
                left join Tag t on t.id = p.tag.id
                group by p.id
            """)
    Page<Object[]> findAllWithLikesAndComments(Pageable pageable);
}