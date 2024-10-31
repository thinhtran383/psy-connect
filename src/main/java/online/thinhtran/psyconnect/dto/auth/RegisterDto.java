package online.thinhtran.psyconnect.dto.auth;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private LocalDate dob;
    private String address;
    private String specialization;
    private String role;
    private Integer experienceYears;

}
