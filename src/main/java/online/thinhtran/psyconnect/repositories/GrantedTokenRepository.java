package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.GrantedToken;
import org.springframework.data.repository.CrudRepository;

public interface GrantedTokenRepository extends CrudRepository<GrantedToken, String> {
    boolean existsById(String token);
}
