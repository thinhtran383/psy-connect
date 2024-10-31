package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.Post;
import online.thinhtran.psyconnect.repositories.PostRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.post.PostResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;


    @Transactional(readOnly = true)
    @Cacheable(value = "postCache", key = "#page")
    public PageableResponse<PostResponse> getAllPost(int page, int size) {
        Page<Object[]> postDetails = postRepository.findAllWithLikesAndComments(PageRequest.of(page, size));

        List<PostResponse> postResponses = postDetails.stream().map(detail -> {
            Post post = (Post) detail[0];
            long likeCount = (long) detail[1];
            long commentCount = (long) detail[2];
            String author = (String) detail[3];
            String tag = (String) detail[4];

            return PostResponse.builder()
                    .id(post.getId())
                    .content(post.getContent())
                    .totalLikes(likeCount)
                    .totalComment(commentCount)
                    .author(author)
                    .createdAt(post.getCreatedAt())
                    .tag(tag)
                    .title(post.getTitle())
                    .build();
        }).collect(Collectors.toList());

        return PageableResponse.<PostResponse>builder()
                .elements(postResponses)
                .totalElements(postDetails.getTotalElements())
                .totalPages(postDetails.getTotalPages())
                .build();
    }
}
