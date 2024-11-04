package online.thinhtran.psyconnect.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private String title;
    private String content;
    private Integer tagId;

    private MultipartFile image;
}
