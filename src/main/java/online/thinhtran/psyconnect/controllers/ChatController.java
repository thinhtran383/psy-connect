package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.dto.chat.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);

        log.info("Message sender to: {}", message.getSenderName());
        log.info("Message sent to: {}", message.getReceiverName());
        log.info("Message: {}", message.getMessage());

        return message;
    }
}
