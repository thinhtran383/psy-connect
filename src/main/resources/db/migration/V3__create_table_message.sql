CREATE TABLE Messages
(
    message_id  INT AUTO_INCREMENT PRIMARY KEY,
    sender_id   INT  NOT NULL,
    receiver_id INT  NOT NULL,
    content     TEXT NOT NULL,
    timestamp   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sender
        FOREIGN KEY (sender_id) REFERENCES psy.Users (user_id),
    CONSTRAINT fk_receiver
        FOREIGN KEY (receiver_id) REFERENCES psy.Users (user_id)
);