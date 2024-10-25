package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.LoginDto;
import online.thinhtran.psyconnect.dto.RegisterDto;
import online.thinhtran.psyconnect.responses.auth.LoginResponse;
import online.thinhtran.psyconnect.responses.auth.RegisterResponse;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-path}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

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
    public ResponseEntity<Response<String>> approve(@PathVariable Integer id) {
        authService.approveDoctor(id);

        return ResponseEntity.ok(Response.<String>builder()
                .message("Approved account with id: " + id + " successfully")
                .build());
    }

}
