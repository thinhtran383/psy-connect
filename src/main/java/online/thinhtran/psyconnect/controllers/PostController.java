package online.thinhtran.psyconnect.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.Post;
import online.thinhtran.psyconnect.entities.PostLike;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.post.PostDetailResponse;
import online.thinhtran.psyconnect.responses.post.PostResponse;
import online.thinhtran.psyconnect.services.PostLikeService;
import online.thinhtran.psyconnect.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-path}/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostLikeService postLikeService;

    @GetMapping
    public ResponseEntity<Response<PageableResponse<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(Response.<PageableResponse<PostResponse>>builder()
                .data(postService.getAllPost(page, size))
                .build());
    }

    @PutMapping("/like/{postId}")
    public ResponseEntity<Response<?>> like(
            @PathVariable Integer postId,
            @AuthenticationPrincipal User user
    ) {

        postLikeService.likePost(postId, user.getId());
        return ResponseEntity.ok(Response.builder()
                .message("Post liked")
                .build());
    }

    @GetMapping("/details/{postId}")
    public ResponseEntity<Response<PostDetailResponse>> getPostDetail(
            @PathVariable Integer postId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(Response.<PostDetailResponse>builder()
                .data(postService.getPostDetailById(postId, user.getId()))
                .message("Post details retrieved")
                .build());
    }
}
