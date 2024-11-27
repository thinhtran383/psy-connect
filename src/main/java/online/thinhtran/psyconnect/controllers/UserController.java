package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.common.RoleEnum;
import online.thinhtran.psyconnect.dto.user.doctor.UpdateDoctorDto;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.users.UserDetailResponse;
import online.thinhtran.psyconnect.responses.users.UserResponse;
import online.thinhtran.psyconnect.responses.users.doctor.DoctorInfoResponse;
import online.thinhtran.psyconnect.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserDetailResponse>> getUserDetail(@PathVariable Integer userId) {
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

    @GetMapping("/doctors")
    public ResponseEntity<Response<PageableResponse<DoctorInfoResponse>>> getDoctorCatalog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String specialization
    ) {
        return ResponseEntity.ok(
                Response.<PageableResponse<DoctorInfoResponse>>builder()
                        .data(userService.getAllDoctorCatalog(specialization, page, size))
                        .build()
        );
    }

    @GetMapping("/doctors/specializations")
    public ResponseEntity<Response<PageableResponse<String>>> getDoctorSpecializations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                Response.<PageableResponse<String>>builder()
                        .data(userService.getAllSpecialization(page, size))
                        .build()
        );
    }

    @PutMapping(value = "/doctor", consumes = "multipart/form-data")
    public ResponseEntity<Response<?>> updateDoctorProfile(
            @AuthenticationPrincipal User user,
            @ModelAttribute UpdateDoctorDto updateDoctorDto
    ) throws IOException {
        userService.updateUser(updateDoctorDto, user);

        return ResponseEntity.ok(
                Response.builder()
                        .message("Update doctor profile successfully")
                        .build()
        );
    }
}
