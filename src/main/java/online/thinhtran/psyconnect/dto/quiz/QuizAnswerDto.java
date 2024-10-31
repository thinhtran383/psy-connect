package online.thinhtran.psyconnect.dto.quiz;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizAnswerDto {
    private Integer quizId;
    private String answer;
}
