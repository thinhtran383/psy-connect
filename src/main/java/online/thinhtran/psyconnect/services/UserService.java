package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.common.RoleEnum;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.UserRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.users.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#page + '_' + #size + '_' + #roleEnum")
    public PageableResponse<UserResponse> getAllUsers(int page, int size, RoleEnum roleEnum) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));

        List<User> content = users.getContent();

        if (roleEnum != null) {
            content = content.stream()
                    .filter(user -> user.getRole().equals(roleEnum))
                    .toList();
        }

        int totalElements = content.size();
        int totalPages = (totalElements + size - 1) / size;

        return PageableResponse.<UserResponse>builder()
                .data(content.stream()
                        .map(UserResponse::fromEntity)
                        .collect(Collectors.toList()))
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }
}
