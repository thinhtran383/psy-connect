package online.thinhtran.psyconnect.dto.chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Message {
    private String senderName;
    private String receiverName;
    private String message;
}
