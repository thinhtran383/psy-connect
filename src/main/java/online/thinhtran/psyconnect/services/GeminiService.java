package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.ai.AIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {
    private final WebClient webClient;
    private final String sysPrompt;

    @Value("${path-gemini}")
    private String path;

    @Value("${api-key}")
    private String apiKey;


    public AIResponse callGemini(String prompt) {
        log.info("Call Gemini with prompt: {}", prompt);


        AIResponse response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("key", apiKey)
                        .build()
                )
                .acceptCharset(StandardCharsets.UTF_8)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json; charset=UTF-8")
                .bodyValue(String.format("""
                        {
                          "system_instruction": {
                            "parts": {
                              "text": "%s"
                            }
                          },
                          "contents": {
                            "parts": {
                              "text": "%s"
                            }
                          },
                        
                        }
                        """, sysPrompt, prompt))
                .retrieve()

                .bodyToMono(AIResponse.class)
                .block();


        return response;
    }



}


