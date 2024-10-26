package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.RevokeToken;
import online.thinhtran.psyconnect.repositories.RevokeTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RevokeTokenService {
    private final RevokeTokenRepository revokeTokenRepository;

    public void revokeToken(String token){
        revokeTokenRepository.save(new RevokeToken(token));
    }
}
