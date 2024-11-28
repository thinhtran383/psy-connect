package online.thinhtran.psyconnect.responses.quiz;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResultResponse {
    public String question;
    public String answer;

    public String toString() {
        return String.format("\n%s\n%s\n", question, answer);
    }
}
