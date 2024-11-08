package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.common.RoleEnum;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.users.UserDetailResponse;
import online.thinhtran.psyconnect.responses.users.UserResponse;
import online.thinhtran.psyconnect.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<PageableResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) RoleEnum role
    ) {
        return ResponseEntity.ok(
                Response.<PageableResponse<UserResponse>>builder()
                        .data(userService.getAllUsers(page, size, role))
                        .build()
        );
    }

    @GetMapping("/user-detail")
    public ResponseEntity<Response<UserDetailResponse>> getUserDetail(@RequestParam Integer userId) {
        return ResponseEntity.ok(
                Response.<UserDetailResponse>builder()
                        .data(userService.getUserDetail(userId))
                        .build()
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<Response<UserDetailResponse>> getUserProfile(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                Response.<UserDetailResponse>builder()
                        .message("Get user profile successfully")
                        .data(userService.getUserDetail(user.getId()))
                        .build()
        );
    }
}
