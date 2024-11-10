package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.dto.rating.UserRatingDto;
import online.thinhtran.psyconnect.entities.Rating;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.RatingRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.rating.UserRatingResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRatingService {
    private final RatingRepository ratingRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public PageableResponse<UserRatingResponse> getAllRatingByDoctorId(Integer doctorId, int page, int size) {
        Page<UserRatingResponse> ratings = ratingRepository.findAllByDoctorId(doctorId, PageRequest.of(page, size));

        return PageableResponse.<UserRatingResponse>builder()
                .elements(ratings.getContent())
                .totalElements(ratings.getTotalElements())
                .totalPages(ratings.getTotalPages())
                .build();
    }

    @Transactional
    public void rating(User user, UserRatingDto userRatingDto) {
        Rating rating = modelMapper.map(userRatingDto, Rating.class);
        rating.setUser(user);


        userService.updateRating(userRatingDto.getDoctorId(), reCalculateRating(userRatingDto.getDoctorId()));
        ratingRepository.save(rating);
    }

    @Transactional
    protected Float reCalculateRating(Integer doctorId) {
        List<Float> ratings = ratingRepository.getAllRatingByDoctorId(doctorId);

        return ratings.stream().reduce(0f, Float::sum) / ratings.size();
    }
}
