package online.thinhtran.psyconnect.components;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtGenerator {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateToken(User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("roles", roles)
                .withClaim("userId", user.getId())
                .sign(Algorithm.HMAC256(secretKey));

        return token;
    }

    public String extractUsername(String token) {
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

}