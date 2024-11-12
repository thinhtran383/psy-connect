package online.thinhtran.psyconnect.dto.rating;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserRatingDto {
    private Integer doctorId;
    private Integer rating;
    private String review;
}
