package online.thinhtran.psyconnect.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.post.PostResponse;
import online.thinhtran.psyconnect.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Response<PageableResponse<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    )
    {
        return ResponseEntity.ok(Response.<PageableResponse<PostResponse>>builder()
                .data(postService.getAllPost(page, size))
                .build());
    }
}
