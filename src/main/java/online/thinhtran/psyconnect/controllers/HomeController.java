package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.services.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final GeminiService geminiService;
    private final String result;

    @GetMapping("/gemini/{prompt}")
    public ResponseEntity<?> gemini(
            @PathVariable String prompt
    ) {
        return ResponseEntity.ok(geminiService.callGemini(result));
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OidcUser user) {
        return "Welcome, " + user.getFullName();
    }
}
