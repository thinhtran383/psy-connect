package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Rating;
import online.thinhtran.psyconnect.responses.rating.UserRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query(
            """
                        select new online.thinhtran.psyconnect.responses.rating.UserRating(u.username, p.name, r.review, r.rating)
                        from Rating r
                        join User u on r.user.id = u.id
                        join Patient p on p.user.id = u.id
                        where r.doctor.id = :doctorId
                    """
    )
    Page<UserRating> findAllByDoctorId(Integer doctorId, Pageable pageable);
}