package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.Post;
import online.thinhtran.psyconnect.entities.PostLike;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.PostLikeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

    @Transactional(readOnly = true)
    public long countByPostId(Integer postId) {
        return postLikeRepository.countByPost_Id(postId);
    }

    @Transactional
    @CacheEvict(value = "postCache", allEntries = true)
    public void likePost(Integer postId, Integer userId) {
        PostLike postLike = PostLike.builder()
                .post(Post.builder().id(postId).build())
                .user(User.builder().id(userId).build())
                .build();

        postLikeRepository.save(postLike);
    }

    @Transactional
    @CacheEvict(value = "postCache", allEntries = true)
    public void unLikePost(Integer postId, Integer userId) {
        postLikeRepository.deleteByPost_IdAndUser_Id(postId, userId);
    }

    @Transactional(readOnly = true)
    public Boolean isUserLikePost(Integer postId, Integer userId) {
        return postLikeRepository.existsByPost_IdAndUser_Id(postId, userId);
    }

    @Transactional
    protected void deleteByPostId(Integer postId) {
        postLikeRepository.deleteByPostId(postId);
    }

    @Transactional(readOnly = true)
    public List<Boolean> isLikedPostAndUser(Set<Integer> postId, Integer userId) {
        return postLikeRepository.isLikedPostAndUser(postId, userId);
    }
}
