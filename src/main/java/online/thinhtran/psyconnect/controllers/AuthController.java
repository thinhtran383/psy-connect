package online.thinhtran.psyconnect.controllers;

import com.cloudinary.api.exceptions.BadRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.auth.ChangePasswordDto;
import online.thinhtran.psyconnect.dto.auth.LoginDto;
import online.thinhtran.psyconnect.dto.auth.RegisterDto;
import online.thinhtran.psyconnect.dto.mail.MailDto;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.auth.LoginResponse;
import online.thinhtran.psyconnect.responses.auth.RegisterResponse;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.services.AuthService;
import online.thinhtran.psyconnect.services.MailService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.base-path}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<Response<RegisterResponse>> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(Response.<RegisterResponse>builder()
                .data(authService.register(registerDto))
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(Response.<LoginResponse>builder()
                .data(authService.login(loginDto))
                .message("Login successfully")
                .build());
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Response<String>> approve(@PathVariable Integer id) throws BadRequest {
        authService.approveDoctor(id);

        return ResponseEntity.ok(Response.<String>builder()
                .message("Approved account with id: " + id + " successfully")
                .build());
    }


    @PostMapping("/admin/login")
    public ResponseEntity<Response<LoginResponse>> adminLogin(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(Response.<LoginResponse>builder()
                .data(authService.adminLogin(loginDto))
                .message("Login successfully")
                .build());
    }

    @PutMapping("/change-password")
    public ResponseEntity<Response<?>> changePassword(
            @RequestBody ChangePasswordDto changePasswordDto,
            @AuthenticationPrincipal User user
    ) {
        authService.changePassword(user, changePasswordDto);

        return ResponseEntity.ok(Response.builder()
                .message("Password changed")
                .data(true)
                .build());

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Response<?>> forgotPassword(@RequestBody MailDto mailDto) throws MessagingException, IOException {
        authService.forgotPassword(mailDto);

        return ResponseEntity.ok(Response.builder()
                .message("Forgot password successfully")
                .build());
    }


}
