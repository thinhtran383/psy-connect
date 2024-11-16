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
import java.util.Optional;
import java.util.Set;

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

    @Query("""
            SELECT u.username, m.content, m.timestamp
            FROM Message m JOIN m.sender u 
            WHERE m.sender.username = :currentUsername OR m.receiver.username = :currentUsername 
            AND u.username NOT IN :existingUsernames 
            """)
    List<Object[]> findLastMessagesForUser(@Param("currentUsername") String currentUsername,
                                           @Param("existingUsernames") List<String> existingUsernames);


//    @Query("SELECT m FROM Message m JOIN FETCH m.sender s JOIN FETCH m.receiver r WHERE s.username = :username AND r.username NOT IN :excludedReceivers ORDER BY m.timestamp DESC")
//    List<Message> findMessagesBySenderAndExcludedReceivers(@Param("username") String username, @Param("excludedReceivers") Set<String> excludedReceivers);


    @Query(
            """
                            SELECT m.content, m.timestamp,
                                   r.username,
                                   COALESCE(dReceiver.name, pReceiver.name) AS fullNameReceiver,
                                   s.avatar
                            FROM Message m
                            JOIN m.sender s
                            LEFT JOIN Doctor d ON d.user.id = s.id
                            LEFT JOIN Patient p ON p.user.id = s.id
                            JOIN m.receiver r
                            LEFT JOIN Doctor dReceiver ON dReceiver.user.id = r.id
                            LEFT JOIN Patient pReceiver ON pReceiver.user.id = r.id
                            WHERE s.username = :username
                              AND r.username NOT IN :excludedReceivers
                            ORDER BY m.timestamp DESC
                    """
    )
    List<Object[]> findMessagesBySenderAndExcludedReceivers(@Param("username") String username,
                                                            @Param("excludedReceivers") Set<String> excludedReceivers);


}