package online.thinhtran.psyconnect.entities;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("RevokeToken")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevokeToken {
    @Id
    private String token;
}
