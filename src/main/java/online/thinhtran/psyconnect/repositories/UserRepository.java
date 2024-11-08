package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.common.RoleEnum;
import online.thinhtran.psyconnect.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Page<User> findAll(Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByUsernameIn(Set<String> usernames);


    Page<User> findAllByRole(RoleEnum roleEnum, Pageable pageable);
}