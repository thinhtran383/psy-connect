package online.thinhtran.psyconnect.responses.users;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

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
    private MultipartFile avatar;
}
