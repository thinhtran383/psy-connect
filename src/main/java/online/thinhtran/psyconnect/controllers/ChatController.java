package online.thinhtran.psyconnect.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.chat.MessageDto;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.chat.CategoryChatResponse;
import online.thinhtran.psyconnect.responses.chat.ChatCategoriesResponse;
import online.thinhtran.psyconnect.services.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.base-path}/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<Response<List<MessageDto>>> getMessages(
            @AuthenticationPrincipal User user,
            @RequestParam String receiverName,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        List<MessageDto> messages = chatService.getMessage(user.getUsername(), receiverName);

        return ResponseEntity.ok(Response.<List<MessageDto>>builder()
                .data(messages)
                .message("Messages retrieved successfully")
                .build());
    }

    @GetMapping("/categories")
    public ResponseEntity<Response<List<CategoryChatResponse>>> getChatCategories(
            @AuthenticationPrincipal User user
    ) {
        List<CategoryChatResponse> chatCategories = chatService.getUserChatCategories(user.getUsername());

        return ResponseEntity.ok(Response.<List<CategoryChatResponse>>builder()
                .data(chatCategories)
                .message("Chat categories retrieved successfully")
                .build());
    }
}
