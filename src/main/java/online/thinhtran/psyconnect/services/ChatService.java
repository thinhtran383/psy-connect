package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.dto.chat.MessageDto;
import online.thinhtran.psyconnect.entities.Message;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.MessageRepository;
import online.thinhtran.psyconnect.repositories.UserRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final MessageRepository messageRepository;
    private final RedisTemplate<String, MessageDto> redisTemplate;
    private final UserRepository userRepository;

    public void saveMessageToRedis(MessageDto message) {
        String key = String.format("messages:%s-%s", message.getSenderName(), message.getReceiverName());

        redisTemplate.opsForList().rightPush(key, message);
        redisTemplate.expire(key, 1, java.util.concurrent.TimeUnit.DAYS);
    }


    @Transactional(readOnly = true)
    public PageableResponse<MessageDto> getMessage(String senderName, String receiverName, int page, int size) {
        String key = String.format("messages:%s-%s", senderName, receiverName);

        // get from redis
        if (redisTemplate.hasKey(key) != null) {
            int start = (page - 1) * size;
            int end = start + size - 1;
            List<MessageDto> messageDto = redisTemplate.opsForList().range(key, start, end);
            long totalElements = Optional.ofNullable(redisTemplate.opsForList().size(key)).orElse(0L);
            long totalPages = (totalElements + size - 1) / size;

            return PageableResponse.<MessageDto>builder()
                    .totalPages(totalPages)
                    .totalElements(totalElements)
                    .elements(messageDto)
                    .build();
        }

        Page<MessageDto> messages = messageRepository.findMessagesBetweenUsers(senderName, receiverName, PageRequest.of(page, size));

        return PageableResponse.<MessageDto>builder()
                .elements(messages.getContent())
                .totalElements(messages.getTotalElements())
                .totalPages(messages.getTotalPages())
                .build();


    }

//    private PageableResponse<MessageDto> getMessageFromRedis(String senderName, String receiverName, int page, int size) {
//        String key = String.format("messages:%s-%s", senderName, receiverName);
//
//        int start = (page - 1) * size;
//        int end = start + size - 1;
//
//        List<MessageDto> messageDto = redisTemplate.opsForList().range(key, start, end);
//
//        long totalElements = Optional.ofNullable(redisTemplate.opsForList().size(key)).orElse(0L);
//        long totalPages = (totalElements + size - 1) / size;
//
//        return PageableResponse.<MessageDto>builder()
//                .elements(messageDto)
//                .totalElements(totalElements)
//                .totalPages(totalPages)
//                .build();
//    }

    //    @Scheduled(fixedRate = 3600000)
    public void syncMessagesFromRedisToDb() {
        Set<String> keys = redisTemplate.keys("messages:*");
        if (keys != null) {
            List<MessageDto> allMessages = new ArrayList<>();


            keys.forEach(key -> {
                List<MessageDto> messages = redisTemplate.opsForList().range(key, 0, -1);
                if (messages != null) {
                    allMessages.addAll(messages);
                }
                redisTemplate.delete(key);
            });

            Set<String> usernames = allMessages.stream()
                    .flatMap(msg -> Stream.of(msg.getSenderName(), msg.getReceiverName()))
                    .collect(Collectors.toSet());

            Map<String, User> userMap = userRepository.findByUsernameIn(usernames)
                    .stream()
                    .collect(Collectors.toMap(User::getUsername, Function.identity()));

            List<Message> messagesToSave = new ArrayList<>();

            allMessages.forEach(messageDto -> {
                User sender = userMap.get(messageDto.getSenderName());
                User receiver = userMap.get(messageDto.getReceiverName());

                if (sender != null && receiver != null) {
                    Message message = new Message();
                    message.setSender(sender);
                    message.setReceiver(receiver);
                    message.setContent(messageDto.getMessage());
                    messagesToSave.add(message);
                }
            });

            messageRepository.saveAll(messagesToSave);
        }

        log.info("Sync messages from Redis to DB");
    }

}