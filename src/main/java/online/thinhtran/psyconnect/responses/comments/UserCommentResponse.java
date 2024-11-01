package online.thinhtran.psyconnect.responses.comments;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCommentResponse {
    private String username;
    private String content;
}
