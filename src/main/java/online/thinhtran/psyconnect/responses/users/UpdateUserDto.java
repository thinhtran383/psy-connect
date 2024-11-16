package online.thinhtran.psyconnect.responses.users;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateUserDto {
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDate dob;
    private String avatar;

}
