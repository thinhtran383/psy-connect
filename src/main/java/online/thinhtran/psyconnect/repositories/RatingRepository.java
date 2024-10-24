package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
}