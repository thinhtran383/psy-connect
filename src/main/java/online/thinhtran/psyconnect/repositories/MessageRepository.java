package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.dto.chat.MessageDto;
import online.thinhtran.psyconnect.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query(
            """
                        select new  online.thinhtran.psyconnect.dto.chat.MessageDto(
                            m.sender.username,
                            m.receiver.username,
                            m.content,
                            m.timestamp
                        ) from Message m
                        where (m.sender.username = :senderName and m.receiver.username = :receiverName) or
                              (m.sender.username = :receiverName and m.receiver.username = :senderName)
                    """
    )
    Page<MessageDto> findMessagesBetweenUsers(String senderName, String receiverName, Pageable pageable);

    @Query("SELECT u.username, m.content, m.timestamp " +
            "FROM Message m JOIN m.sender u " +
            "WHERE m.sender.username = :currentUsername OR m.receiver.username = :currentUsername " +
            "AND u.username NOT IN :existingUsernames ")
    List<Object[]> findLastMessagesForUser(@Param("currentUsername") String currentUsername,
                                           @Param("existingUsernames") List<String> existingUsernames);
}