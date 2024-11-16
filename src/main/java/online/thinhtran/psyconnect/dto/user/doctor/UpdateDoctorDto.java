package online.thinhtran.psyconnect.dto.user.doctor;

import lombok.*;
import online.thinhtran.psyconnect.responses.users.UpdateUserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDoctorDto extends UpdateUserDto {
    private String about;
    private String degree;
}
