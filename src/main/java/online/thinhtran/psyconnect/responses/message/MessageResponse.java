package online.thinhtran.psyconnect.responses.message;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private String senderName;
    private String receiverName;
    private String message;
    private LocalDateTime timestamp;
}
