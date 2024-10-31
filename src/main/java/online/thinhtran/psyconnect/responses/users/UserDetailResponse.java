package online.thinhtran.psyconnect.responses.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import online.thinhtran.psyconnect.entities.Doctor;
import online.thinhtran.psyconnect.entities.Patient;
import online.thinhtran.psyconnect.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailResponse {
    private String name;
    private String email;
    private String address;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String specialization;
    private String role;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;


}
