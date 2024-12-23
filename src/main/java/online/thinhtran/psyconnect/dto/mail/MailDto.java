package online.thinhtran.psyconnect.dto.mail;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import java.util.Map;

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
    private Map<String, Object> placeholders;
    @Hidden
    private String templateName;

}
