package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.dto.chat.MessageDto;
import online.thinhtran.psyconnect.entities.Message;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.MessageRepository;
import online.thinhtran.psyconnect.repositories.UserRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.chat.ChatCategoriesResponse;
import online.thinhtran.psyconnect.responses.users.UserDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final UserService userService;


    public void saveMessageToRedis(MessageDto message) {
        String key = String.format("messages:%s-%s", message.getSenderName(), message.getReceiverName());

        Map<String, String> avatarAndName = userService.getAvatarAndNameByUsernames(message.getReceiverName());

        log.info("Avatar and name: {}", avatarAndName);

        message.setAvatar(avatarAndName.get("avatar"));
        message.setFullNameReceiver(avatarAndName.get("name"));

        redisTemplate.opsForList().rightPush(key, message);
        redisTemplate.expire(key, 1, java.util.concurrent.TimeUnit.DAYS);
    }


    @Transactional(readOnly = true)
    public PageableResponse<MessageDto> getMessage(String senderName, String receiverName, int page, int size) {
        String key = String.format("messages:%s-%s", senderName, receiverName);

        log.info("Key: {}", redisTemplate.hasKey(key));

        // get from redis
        if (redisTemplate.hasKey(key) != null && Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            int start = (page - 1) * size;
            int end = start + size - 1;
            List<MessageDto> messageDto = redisTemplate.opsForList().range(key, start, end);

            if (messageDto != null) {
                messageDto.sort(Comparator.comparing(MessageDto::getTimestamp).reversed());
            }

            long totalElements = Optional.ofNullable(redisTemplate.opsForList().size(key)).orElse(0L);
            long totalPages = (totalElements + size - 1) / size;

            return PageableResponse.<MessageDto>builder()
                    .totalPages(totalPages)
                    .totalElements(totalElements)
                    .elements(messageDto)
                    .build();
        }


        // get from db
        Page<MessageDto> messages = messageRepository.findMessagesBetweenUsers(
                senderName,
                receiverName,
                PageRequest.of(page, size, Sort.by("timestamp").descending())
        );


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

            Map<String, User> userMap = userService.getUsersByUsernames(usernames)
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
                    message.setTimestamp(messageDto.getTimestamp());
                    messagesToSave.add(message);
                }
            });

            messageRepository.saveAll(messagesToSave);
        }

        log.info("Sync messages from Redis to DB");
    }


    @Transactional(readOnly = true)
    public List<ChatCategoriesResponse> getChatCategories(String username) {
        List<ChatCategoriesResponse> result = new ArrayList<>();

        // Get all keys where the user is either the sender or the receiver
        Set<String> keys = new HashSet<>();
        keys.addAll(redisTemplate.keys("messages:" + username + "-*"));  // as sender
        keys.addAll(redisTemplate.keys("messages:*-" + username));       // as receiver

        Set<String> existingChatUsers = new HashSet<>();

        if (keys != null) {
            keys.forEach(key -> {
                String[] split = key.split("messages:")[1].split("-");
                String senderName = split[0];
                String receiverName = split[1];

                String chatPartner = senderName.equals(username) ? receiverName : senderName;
                existingChatUsers.add(chatPartner);

                // get last message from Redis
                MessageDto lastMessage = Optional.ofNullable(redisTemplate.opsForList().range(key, -1, -1))
                        .map(messages -> messages.get(0))
                        .orElse(null);

                if (lastMessage != null) {
                    ChatCategoriesResponse chatCategoriesResponse = ChatCategoriesResponse.builder()
                            .lastMessage(lastMessage.getMessage())
                            .username(chatPartner)
                            .fullName(lastMessage.getFullNameReceiver())
                            .avatar(lastMessage.getAvatar())
                            .lastMessageTime(lastMessage.getTimestamp())
                            .build();

                    result.add(chatCategoriesResponse);
                }
            });
        }

        // Get missing messages from DB for chats not present in Redis
        List<Object[]> missingMessages = messageRepository.findMessagesBySenderAndExcludedReceivers(username, existingChatUsers);

        missingMessages.forEach(message -> {
            ChatCategoriesResponse chatCategoriesResponse = ChatCategoriesResponse.builder()
                    .lastMessage((String) message[0])
                    .lastMessageTime((LocalDateTime) message[1])
                    .username((String) message[2])
                    .fullName((String) message[3])
                    .avatar((String) message[4])
                    .build();

            result.add(chatCategoriesResponse);
        });

        return result;
    }


}