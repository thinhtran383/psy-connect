package online.thinhtran.psyconnect.responses.auth;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DoctorRegisterResponse extends RegisterResponse {
    private String specialization;
}
