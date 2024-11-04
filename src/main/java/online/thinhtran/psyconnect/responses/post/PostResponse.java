package online.thinhtran.psyconnect.responses.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private Integer id;
    private String title;
    private String author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String content;
    private String tag;
    private Long totalComment;
    private Long totalLikes;
    private String avatar;
    private String thumbnail;

}
