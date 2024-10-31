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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserAnswer", schema = "psy")
public class UserAnswer {
    @Id
    @Column(name = "response_id", nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "quiz_id", nullable = false)
    private Integer quizId;

    @Size(max = 255)
    @NotNull
    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.id = (int) (Math.random() * 1000000);
    }

}