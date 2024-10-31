package online.thinhtran.psyconnect.responses.quiz;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizResponse {
    private Integer id;
    private String question;
    private List<String> choice;

}
