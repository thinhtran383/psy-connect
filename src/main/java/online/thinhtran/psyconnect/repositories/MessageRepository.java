package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
  }