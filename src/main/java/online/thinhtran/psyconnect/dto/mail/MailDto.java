package online.thinhtran.psyconnect.dto.mail;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailDto {
    private String to;
    @Hidden
    private String subject;
    @Hidden
    private String content;
}
