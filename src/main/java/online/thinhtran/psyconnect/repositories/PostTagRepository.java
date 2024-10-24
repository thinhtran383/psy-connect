package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.PostTag;
import online.thinhtran.psyconnect.entities.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
}