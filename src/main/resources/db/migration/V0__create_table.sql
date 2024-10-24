-- Tạo bảng `users` để lưu trữ thông tin người dùng
CREATE TABLE Users
(
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(255)                        NOT NULL,
    email      VARCHAR(255)                        NOT NULL UNIQUE,
    password   VARCHAR(255)                        NOT NULL,
    role       ENUM ('DOCTOR', 'PATIENT', 'ADMIN') NOT NULL,
    status     ENUM ('pending', 'approved', 'rejected') DEFAULT 'pending',
    created_at TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng `quizzes` để lưu trữ câu hỏi quiz
CREATE TABLE Quizzes
(
    quiz_id        INT AUTO_INCREMENT PRIMARY KEY,
    question       TEXT         NOT NULL,
    option_1       VARCHAR(255) NOT NULL,
    option_2       VARCHAR(255) NOT NULL,
    option_3       VARCHAR(255) NOT NULL,
    option_4       VARCHAR(255) NOT NULL,
    correct_answer VARCHAR(255),
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng `user_quiz_responses` để lưu trữ kết quả quiz của người dùng
CREATE TABLE UserAnswer
(
    response_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT,
    quiz_id     INT,
    answer      VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (user_id),
    FOREIGN KEY (quiz_id) REFERENCES Quizzes (quiz_id)
);

-- tao bang 'patients' de luu tru thong tin benh nhan
CREATE TABLE Patients
(
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT UNIQUE,
    name       VARCHAR(255) NOT NULL,
    dob        DATE,
    address    VARCHAR(255),
    phone      varchar(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng `doctors` để lưu trữ thông tin bác sĩ
CREATE TABLE Doctors
(
    doctor_id           INT AUTO_INCREMENT PRIMARY KEY,
    user_id             INT UNIQUE,
    name                VARCHAR(255) NOT NULL,
    address             VARCHAR(255),
    phone               VARCHAR(255),
    dob                 DATE,
    specialization      VARCHAR(255) NOT NULL,
    experience_years    INT          NOT NULL,
    rating              FLOAT     DEFAULT 0,
    total_ratings       INT       DEFAULT 0,
    successful_sessions INT       DEFAULT 0,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (user_id)
);

-- Tạo bảng `ratings` để lưu trữ đánh giá của người dùng cho bác sĩ
CREATE TABLE Ratings
(
    rating_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT,
    doctor_id  INT,
    rating     FLOAT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review     TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (user_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctors (doctor_id)
);

-- Tạo bảng `posts` để lưu trữ bài đăng
CREATE TABLE Posts
(
    post_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (user_id)
);

-- Tạo bảng `tags` để lưu trữ các thẻ tag
CREATE TABLE Tags
(
    tag_id   INT AUTO_INCREMENT PRIMARY KEY,
    tag_name VARCHAR(100) NOT NULL UNIQUE
);

-- Tạo bảng `post_tags` để gán thẻ tag vào bài viết
CREATE TABLE PostTags
(
    post_id INT,
    tag_id  INT,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES Posts (post_id),
    FOREIGN KEY (tag_id) REFERENCES Tags (tag_id)
);

-- Tạo bảng `comments` để lưu trữ các bình luận trên bài viết
CREATE TABLE Comments
(
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id    INT,
    user_id    INT,
    content    TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES Posts (post_id),
    FOREIGN KEY (user_id) REFERENCES Users (user_id)
);

-- Tạo bảng `post_likes` để lưu trữ các lượt thả tim trên bài viết
CREATE TABLE PostLikes
(
    like_id    INT AUTO_INCREMENT PRIMARY KEY,
    post_id    INT,
    user_id    INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES Posts (post_id),
    FOREIGN KEY (user_id) REFERENCES Users (user_id)
);
