package online.thinhtran.psyconnect.responses.comments;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCommentResponse {
    private String fullName;
    private String username;
    private String content;
    private String avatar;
}
