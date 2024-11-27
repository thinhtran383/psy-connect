package online.thinhtran.psyconnect.responses.ai;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AIResponse {
    // Getters và Setters
    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;
    private String modelVersion;

}

@Setter
@Getter
class Candidate {
    // Getters và Setters
    private Content content;
    private String finishReason;
    private double avgLogprobs;

}

@Setter
@Getter
class Content {
    private List<Part> parts;
    private String role;

}

@Setter
@Getter
class Part {
    private String text;

}

@Setter
@Getter
class UsageMetadata {
    private int promptTokenCount;
    private int candidatesTokenCount;
    private int totalTokenCount;

}
