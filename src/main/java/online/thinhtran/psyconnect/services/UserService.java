package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
