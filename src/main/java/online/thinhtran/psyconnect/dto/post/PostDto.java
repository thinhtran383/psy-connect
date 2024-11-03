package online.thinhtran.psyconnect.dto.post;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private String title;
    private String content;
    private Integer tagId;
}
