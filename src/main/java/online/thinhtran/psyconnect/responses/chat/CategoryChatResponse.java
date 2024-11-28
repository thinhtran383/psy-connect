package online.thinhtran.psyconnect.responses.chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryChatResponse {
    private String receiverName;
    private String fullNameReceiver;
    private String avatar;
    private String lastMessage;
    private String lastMessageTime;

}
