package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.GrantedToken;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.CrudRepository;

public interface GrantedTokenRepository extends KeyValueRepository<GrantedToken, String> {
    boolean existsById(String token);
}
