-- Thêm cột tag_id vào bảng Posts
ALTER TABLE Posts
    ADD COLUMN tag_id INT;

-- Tạo ràng buộc khóa ngoại từ tag_id trong Posts tới tag_id trong Tags
ALTER TABLE Posts
    ADD CONSTRAINT fk_post_tag
        FOREIGN KEY (tag_id) REFERENCES Tags(tag_id);

-- Xóa bảng PostTags
DROP TABLE IF EXISTS PostTags;
