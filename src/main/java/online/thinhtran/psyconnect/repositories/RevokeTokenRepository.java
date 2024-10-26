package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.RevokeToken;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.CrudRepository;

public interface RevokeTokenRepository extends KeyValueRepository<RevokeToken, String> {
    boolean existsByToken(String token);
}
