package online.thinhtran.psyconnect.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Quizzes", schema = "psy")
public class Quiz {
    @Id
    @Column(name = "quiz_id", nullable = false)
    private Integer id;

    @NotNull
    @Lob
    @Column(name = "question", nullable = false)
    private String question;

    @Size(max = 255)
    @NotNull
    @Column(name = "option_1", nullable = false)
    private String option1;

    @Size(max = 255)
    @NotNull
    @Column(name = "option_2", nullable = false)
    private String option2;

    @Size(max = 255)
    @NotNull
    @Column(name = "option_3", nullable = false)
    private String option3;

    @Size(max = 255)
    @NotNull
    @Column(name = "option_4", nullable = false)
    private String option4;

    @Size(max = 255)
    @NotNull
    @Column(name = "option_5", nullable = false)
    private String option5;

    @Size(max = 255)
    @NotNull
    @Column(name = "option_6", nullable = false)
    private String option6;

    @Size(max = 255)
    @NotNull
    @Column(name = "option_7", nullable = false)
    private String option7;

    @Size(max = 255)
    @NotNull
    @Column(name = "option_8", nullable = false)
    private String option8;


    @Size(max = 255)
    @Column(name = "correct_answer")
    private String correctAnswer;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}