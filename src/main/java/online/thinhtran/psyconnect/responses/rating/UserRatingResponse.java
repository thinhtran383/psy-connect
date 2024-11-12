package online.thinhtran.psyconnect.responses.rating;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRatingResponse {
    private String username;
    private String fullName;
    private String content;
    private Float rating;
    private String avatar;
}
