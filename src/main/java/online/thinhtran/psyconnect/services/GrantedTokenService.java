package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.GrantedToken;
import online.thinhtran.psyconnect.repositories.GrantedTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrantedTokenService {
    private final GrantedTokenRepository grantedTokenRepository;

    public void grantToken(String token){
        grantedTokenRepository.save(new GrantedToken(token));
    }

    public boolean isGranted(String token){
        return grantedTokenRepository.existsById(token);
    }

    public void revokeToken(String token){
        if(grantedTokenRepository.existsById(token)){
            grantedTokenRepository.delete(new GrantedToken(token));
        }
    }
}
