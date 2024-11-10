package online.thinhtran.psyconnect.responses.users.doctor;

import lombok.*;
import lombok.experimental.SuperBuilder;
import online.thinhtran.psyconnect.responses.users.UserDetailResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DoctorDetailResponse extends UserDetailResponse {
    private List<String> certificates;
    private String specialization;
    private String about;
    private String degree;
    private Float avgRating;
    private Integer experience;
}
