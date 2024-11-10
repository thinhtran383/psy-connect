package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.repositories.RatingRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.rating.UserRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRatingService {
    private final RatingRepository ratingRepository;


    @Transactional(readOnly = true)
    public PageableResponse<UserRating> getAllRatingByDoctorId(Integer doctorId, int page, int size) {
        Page<UserRating> ratings = ratingRepository.findAllByDoctorId(doctorId, PageRequest.of(page, size));

        return PageableResponse.<UserRating>builder()
                .elements(ratings.getContent())
                .totalElements(ratings.getTotalElements())
                .totalPages(ratings.getTotalPages())
                .build();
    }
}
