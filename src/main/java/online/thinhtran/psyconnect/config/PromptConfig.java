package online.thinhtran.psyconnect.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Slf4j
public class PromptConfig {
    @Bean
    public String sysPrompt(ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:sys-prompt.txt");

        log.info("Load system prompt content from: {}", Files.readString(Paths.get(resource.getURI())));

        return Files.readString(Paths.get(resource.getURI()));
    }
}
