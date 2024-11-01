package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.Post;
import online.thinhtran.psyconnect.repositories.PostRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.comments.UserCommentResponse;
import online.thinhtran.psyconnect.responses.post.PostDetailResponse;
import online.thinhtran.psyconnect.responses.post.PostResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;

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

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetailById(Integer postId, Integer userId) {
//        List<UserCommentResponse> comments = commentService.getAllCommentsByPostId(postId);

        Object[] postDetails = (Object[]) postRepository.findAllWithLikeAndCommentsByPostId(postId, userId);

        return PostDetailResponse.builder()
//                .commentResponses(comments)
                .id(postId)
                .title((String) postDetails[0])
                .content((String) postDetails[1])
                .tag((String) postDetails[2])
                .likeCount((Long) postDetails[3])
                .commentCount((Long) postDetails[4])
                .author((String) postDetails[5])
                .createdAt((LocalDateTime) postDetails[6])
                .updatedAt((LocalDateTime) postDetails[7])
                .liked((Boolean) postDetails[8])
                .build();
    }

}
