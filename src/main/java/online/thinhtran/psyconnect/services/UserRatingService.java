package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.dto.rating.UserRatingDto;
import online.thinhtran.psyconnect.entities.Doctor;
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
@Slf4j
public class UserRatingService {
    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public PageableResponse<UserRatingResponse> getAllRatingByDoctorId(Integer userId, int page, int size) {
        Page<UserRatingResponse> ratings = ratingRepository.findAllRatingByUserId(userId, PageRequest.of(page, size));

        return PageableResponse.<UserRatingResponse>builder()
                .elements(ratings.getContent())
                .totalElements(ratings.getTotalElements())
                .totalPages(ratings.getTotalPages())
                .build();
    }

    @Transactional
    public void rating(User user, UserRatingDto userRatingDto) {
        Doctor doctor = userService.getUserIdByDoctorId(userRatingDto.getDoctorId());

        Rating rating = modelMapper.map(userRatingDto, Rating.class);
        rating.setUser(user);
        rating.setDoctor(doctor);

        ratingRepository.save(rating);

        Float updatedRating = reCalculateRating(doctor.getId());
        userService.updateRating(doctor.getId(), updatedRating);
    }

    @Transactional
    protected Float reCalculateRating(Integer doctorId) {
        List<Float> ratings = ratingRepository.getAllRatingByDoctorId(doctorId);

        if (ratings.isEmpty()) {
            return 0.0f;
        }

        return ratings.stream().reduce(0f, Float::sum) / ratings.size();
    }

}
