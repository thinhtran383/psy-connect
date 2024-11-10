package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.dto.post.PostDto;
import online.thinhtran.psyconnect.entities.Post;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.PostRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.post.PostDetailResponse;
import online.thinhtran.psyconnect.responses.post.PostResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final PostLikeService postLikeService;
    private final TagService tagService;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    @Cacheable(value = "postCache", key = "#page + '_' + #page + '_' + #size")
    public PageableResponse<PostResponse> getAllPost(int page, int size) {
        Page<Object[]> postDetails = postRepository.findAllWithLikesAndComments(PageRequest.of(page, size));


        List<PostResponse> postResponses = getPostResponse(postDetails).collect(Collectors.toList());

        return PageableResponse.<PostResponse>builder()
                .elements(postResponses)
                .totalElements(postDetails.getTotalElements())
                .totalPages(postDetails.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetailById(Integer postId, Integer userId) {
//        List<UserCommentResponse> comments = commentService.getAllCommentsByPostId(postId);

        log.error("User id: {}", userId);

        Object[] postDetails = (Object[]) postRepository.findAllWithLikeAndCommentsByPostId(postId);

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
                .liked(postLikeService.isUserLikePost(postId, userId))
                .build();
    }


    @Transactional(readOnly = true)
    public PageableResponse<PostResponse> getPostLikedByUserId(Integer userId, int page, int size) {
        Page<Object[]> postLiked = postRepository.findPostLikedByUserId(userId, PageRequest.of(page, size));

        List<PostResponse> postResponses = getPostResponse(postLiked).toList();

        return PageableResponse.<PostResponse>builder()
                .elements(postResponses)
                .totalElements(postLiked.getTotalElements())
                .totalPages(postLiked.getTotalPages())
                .build();
    }

    @Transactional
    @CacheEvict(value = "postCache", allEntries = true)
    public PostResponse createPost(PostDto postDto, User user) throws IOException {
        byte[] image = postDto.getImage().getBytes();

        String thumbnail = cloudinaryService.upload(image);

        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .tagId(postDto.getTagId())
                .user(user)
                .thumbnail(thumbnail)
                .build();

        postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .totalComment(0L)
                .totalLikes(0L)
                .author(user.getUsername())
                .content(post.getContent())
                .title(post.getTitle())
                .thumbnail(post.getThumbnail())
                .tag(tagService.getTagById(postDto.getTagId()))
                .createdAt(LocalDateTime.now())
                .build();


    }


    @Transactional(readOnly = true)
    @Cacheable(value = "postCache", key = "#userId + '_' + #page + '_' + #size")
    public PageableResponse<PostResponse> getOwnerPost(Integer userId, int page, int size) {
        Page<Object[]> postOwner = postRepository.findPostCreateByUserId(userId, PageRequest.of(page, size));

        List<PostResponse> postResponses = getPostResponse(postOwner).toList();

        return PageableResponse.<PostResponse>builder()
                .elements(postResponses)
                .totalElements(postOwner.getTotalElements())
                .totalPages(postOwner.getTotalPages())
                .build();
    }

    private Stream<PostResponse> getPostResponse(Page<Object[]> postLiked) {
        return postLiked.stream().map(detail -> {
            Post post = (Post) detail[0];
            long likeCount = (long) detail[1];
            long commentCount = (long) detail[2];
            String author = (String) detail[3];
            String tag = (String) detail[4];
            String thumbnail = (String) detail[5];
            String avatar = (String) detail[6];

            return PostResponse.builder()
                    .id(post.getId())
                    .content(post.getContent())
                    .totalLikes(likeCount)
                    .totalComment(commentCount)
                    .author(author)
                    .createdAt(post.getCreatedAt())
                    .tag(tag)
                    .title(post.getTitle())
                    .avatar(avatar)
                    .thumbnail(thumbnail)
                    .build();
        });
    }

    @Transactional
    @CacheEvict(value = "postCache", allEntries = true)
    public void deletePost(Integer postId) {
        commentService.deleteCommentsByPostId(postId);
        postLikeService.deleteByPostId(postId);
        postRepository.deletePostById(postId);
    }

    @Transactional
    @CacheEvict(value = "postCache", allEntries = true)
    public void deleteOwnPost(Integer postId, Integer id) {
        commentService.deleteCommentsByPostId(postId);
        postLikeService.deleteByPostId(postId);
        postRepository.deletePostByIdAndUserId(postId, id);
    }
}
