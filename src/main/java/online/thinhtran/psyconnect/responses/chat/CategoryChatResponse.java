package online.thinhtran.psyconnect.responses.chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryChatResponse {
    private String username;
    private String fullName;
    private String avatar;
    private String lastMessage;
    private String lastMessageTime;

}
