package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.comments.CommentDto;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.comments.UserCommentResponse;
import online.thinhtran.psyconnect.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-path}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    @GetMapping("/details/{postId}")
    public ResponseEntity<Response<PageableResponse<UserCommentResponse>>> getCommentByPostId(
            @PathVariable Integer postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(Response.<PageableResponse<UserCommentResponse>>builder()
                .data(commentService.getAllCommentsByPostId(postId, page, size))
                .message("Comments fetched successfully")
                .build());
    }

    @PostMapping
    public ResponseEntity<Response<?>> addComment(
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal User user
    ) {
        commentService.commentPost(commentDto, user);

        return ResponseEntity.ok(Response.builder()
                .message("Comment added")
                .build());
    }
}
