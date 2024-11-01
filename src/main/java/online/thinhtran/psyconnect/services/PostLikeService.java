package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.Post;
import online.thinhtran.psyconnect.entities.PostLike;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.PostLikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

    @Transactional(readOnly = true)
    public long countByPostId(Integer postId){
        return postLikeRepository.countByPost_Id(postId);
    }

    @Transactional
    public void likePost(Integer postId, Integer userId){
        PostLike postLike = PostLike.builder()
                .post(Post.builder().id(postId).build())
                .user(User.builder().id(userId).build())
                .build();

        postLikeRepository.save(postLike);
    }
}
