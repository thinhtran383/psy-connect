package online.thinhtran.psyconnect.responses.rating;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRating {
    private String username;
    private String fullName;
    private String content;
    private Float rating;
}
