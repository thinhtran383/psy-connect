package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.dto.comments.CommentDto;
import online.thinhtran.psyconnect.dto.rating.UserRatingDto;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.comments.UserCommentResponse;
import online.thinhtran.psyconnect.responses.rating.UserRatingResponse;
import online.thinhtran.psyconnect.services.CommentService;
import online.thinhtran.psyconnect.services.UserRatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-path}/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;
    private final UserRatingService userRatingService;


    @GetMapping("/{postId}")
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

    @GetMapping("/rating/{userId}")
    public ResponseEntity<Response<PageableResponse<UserRatingResponse>>> getRatingAndReviewByDoctorId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(Response.<PageableResponse<UserRatingResponse>>builder()
                .data(userRatingService.getAllRatingByDoctorId(userId, page, size))
                .message("Ratings fetched successfully")
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

    @PostMapping("/rating")
    public ResponseEntity<Response<?>> addRating(
            @RequestBody UserRatingDto userRatingDto,
            @AuthenticationPrincipal User user
    ) {

        log.info("Rating: {}", userRatingDto);

        userRatingService.rating(user, userRatingDto);

        return ResponseEntity.ok(Response.builder()
                .message("Rating added")
                .data(true)
                .build()
        );
    }
}
