CREATE TABLE Threads (
                         thread_id INT AUTO_INCREMENT PRIMARY KEY,
                         user1_id INT NOT NULL,
                         user2_id INT NOT NULL,
                         last_message_id INT,
                         last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         CONSTRAINT unique_thread UNIQUE (user1_id, user2_id),
                         CONSTRAINT fk_user1 FOREIGN KEY (user1_id) REFERENCES Users (user_id),
                         CONSTRAINT fk_user2 FOREIGN KEY (user2_id) REFERENCES Users (user_id),
                         CONSTRAINT fk_last_message FOREIGN KEY (last_message_id) REFERENCES Messages (message_id)
);

ALTER TABLE Messages
    ADD COLUMN thread_id INT NOT NULL,
    ADD CONSTRAINT fk_thread FOREIGN KEY (thread_id) REFERENCES Threads (thread_id);


