package online.thinhtran.psyconnect.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import online.thinhtran.psyconnect.responses.users.UserDetailResponse;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MessageDto {
    private String senderName;
    private String receiverName;
    private String message;

    private String fullNameReceiver;
    private String avatar;

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    public MessageDto(String senderName, String receiverName, String message, LocalDateTime timestamp) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.message = message;
        this.timestamp = timestamp;
    }
}
