package online.thinhtran.psyconnect.responses.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import online.thinhtran.psyconnect.responses.comments.UserCommentResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailResponse {
    private Integer id;
    private String author;
    private String title;
    private String content;
    private String tag;
    private Long likeCount;
    private Long commentCount;
    private Boolean liked;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

//    private List<UserCommentResponse> commentResponses;
}
