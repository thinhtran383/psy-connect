package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
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
}
