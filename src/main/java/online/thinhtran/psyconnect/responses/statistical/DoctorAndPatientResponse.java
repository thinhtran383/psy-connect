package online.thinhtran.psyconnect.responses.statistical;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorAndPatientResponse {
    private long doctor;
    private long patient;
}
