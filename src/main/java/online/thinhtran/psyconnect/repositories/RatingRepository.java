package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Rating;
import online.thinhtran.psyconnect.responses.rating.UserRatingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query(
            """
                        select new online.thinhtran.psyconnect.responses.rating.UserRatingResponse(u.username, p.name, r.review, r.rating, u.avatar)
                        from Rating r
                        join User u on r.user.id = u.id
                        join Patient p on p.user.id = u.id
                        join Doctor d on r.doctor.id = d.id
                        where d.user.id = :userId
                    """
    )
    Page<UserRatingResponse> findAllRatingByUserId(Integer userId, Pageable pageable);


    @Query(
            """
                        select r.rating from Rating r where r.doctor.id = :doctorId
                    """
    )
    List<Float> getAllRatingByDoctorId(Integer doctorId);
}