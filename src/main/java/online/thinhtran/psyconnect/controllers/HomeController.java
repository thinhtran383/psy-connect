package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.services.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final GeminiService geminiService;

    @GetMapping("/gemini")
    public ResponseEntity<?> gemini() {
        return ResponseEntity.ok(geminiService.callGemini("Giải thích về tâm lý học"));
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OidcUser user) {
        return "Welcome, " + user.getFullName();
    }
}
