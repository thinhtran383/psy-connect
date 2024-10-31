package online.thinhtran.psyconnect.components;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.services.GrantedTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtGenerator {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private final GrantedTokenService grantedTokenService;

    public String generateToken(User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", roles)
                .withClaim("userId", user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 864000000)) // Expires in 10 days
                .sign(Algorithm.HMAC256(secretKey));

        return token;
    }

    public String extractUsername(String token) throws SignatureVerificationException {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getSubject();
    }

    public int extractUserId(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getClaim("userId")
                .asInt();
    }

    public List<Role> extractRoles(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getClaim("roles")
                .asList(Role.class);
    }

    public boolean isValidToken(String token) {
        try {
            if (isTokenExpired(token)) {
                grantedTokenService.revokeToken(token);
                return false;
            }

            boolean isGranted = grantedTokenService.isGranted(token);
            log.info("Token is granted: {}", isGranted);
            if (!isGranted) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private boolean isTokenExpired(String token) {
        // Check the expiration date of the token
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getExpiresAt()
                .before(new Date());
    }
}
