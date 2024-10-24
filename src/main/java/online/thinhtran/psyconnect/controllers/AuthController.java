package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.RegisterDto;
import online.thinhtran.psyconnect.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(authService.register(registerDto));
    }
}
