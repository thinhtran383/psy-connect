package online.thinhtran.psyconnect.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostDto {
    private String title;
    private String content;
    private MultipartFile thumbnail;
    private Integer tags;
}
