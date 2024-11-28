package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.dto.chat.MessageDto;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.ThreadRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.chat.CategoryChatResponse;
import online.thinhtran.psyconnect.responses.chat.ChatCategoriesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, MessageDto> redisTemplate;
    private final UserService userService;
    private final ThreadRepository threadRepository;


    public void saveMessageToRedis(MessageDto message) {
        String key = String.format("messages:%s-%s", message.getSenderName(), message.getReceiverName());

        Map<String, String> avatarAndName = userService.getAvatarAndNameByUsernames(message.getReceiverName());

        log.info("Avatar and name: {}", avatarAndName);

        message.setAvatar(avatarAndName.get("avatar"));
        message.setFullNameReceiver(avatarAndName.get("name"));

        redisTemplate.opsForList().rightPush(key, message);
        redisTemplate.expire(key, 1, java.util.concurrent.TimeUnit.DAYS);
    }


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
        return null;
    }

    public List<CategoryChatResponse> getUserChatCategories(String userId) {
        // Danh sách các cuộc trò chuyện của người dùng
        List<CategoryChatResponse> categoryChatList = new ArrayList<>();

        // Lấy tất cả các khóa liên quan đến userId
        Set<String> keys = redisTemplate.keys("messages:*");

        // Lọc và xử lý các khóa
        Set<String> uniqueKeys = new HashSet<>();
        for (String key : keys) {
            String[] keyParts = key.split("[:-]");
            String sender = keyParts[1];
            String receiver = keyParts[2];

            // Sắp xếp tên để đảm bảo chỉ lưu một chiều khóa
            String sortedKey = sender.compareTo(receiver) < 0
                    ? String.format("messages:%s-%s", sender, receiver)
                    : String.format("messages:%s-%s", receiver, sender);

            // Nếu khóa đã xử lý, bỏ qua
            if (uniqueKeys.contains(sortedKey)) continue;
            uniqueKeys.add(sortedKey);

            // Kiểm tra nếu userId liên quan đến khóa này
            if (!sender.equals(userId) && !receiver.equals(userId)) continue;

            // Lấy tin nhắn cuối cùng từ Redis
            List<MessageDto> messages = redisTemplate.opsForList().range(sortedKey, 0, -1);
            if (messages != null && !messages.isEmpty()) {
                MessageDto lastMessage = messages.get(messages.size() - 1);

                // Xác định "người kia"
                String otherUser = sender.equals(userId) ? receiver : sender;

                Map<String, String> nameAndAvatar =  userService.getAvatarAndNameByUsernames(otherUser);

                // Tạo đối tượng phản hồi
                CategoryChatResponse categoryChat = new CategoryChatResponse();
                categoryChat.setReceiverName(otherUser);
                categoryChat.setFullNameReceiver(nameAndAvatar.get("name"));
                categoryChat.setAvatar(nameAndAvatar.get("avatar"));
                categoryChat.setLastMessage(lastMessage.getMessage());
                categoryChat.setLastMessageTime(String.valueOf(lastMessage.getTimestamp()));

                categoryChatList.add(categoryChat);
            }
        }

        return categoryChatList;
    }




}