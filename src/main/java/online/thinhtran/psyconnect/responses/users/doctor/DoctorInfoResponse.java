package online.thinhtran.psyconnect.responses.users.doctor;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorInfoResponse {
    private Integer id;
    private String name;
    private String avatar;
    private String specialization;
    private Float rating;
}
