package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Thread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, Integer> {
}