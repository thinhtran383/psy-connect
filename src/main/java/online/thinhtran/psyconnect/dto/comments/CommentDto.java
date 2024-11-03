package online.thinhtran.psyconnect.dto.comments;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Integer postId;
    private String content;
}
