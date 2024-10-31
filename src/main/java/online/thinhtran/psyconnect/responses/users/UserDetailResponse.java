package online.thinhtran.psyconnect.responses.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
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

    public static UserDetailResponse fromEntity(User user) {
        UserDetailResponse userResponse = UserDetailResponse.builder()
                .email(user.getEmail())
                .build();

        if (user.getDoctor() != null) {
            userResponse.setName(user.getDoctor().getName());
            userResponse.setPhone(user.getDoctor().getPhone());
            userResponse.setAddress(user.getDoctor().getAddress());
            userResponse.setDob(user.getDoctor().getDob());
            userResponse.setSpecialization(user.getDoctor().getSpecialization());
            userResponse.setRole(user.getRole().name());

        } else if (user.getPatient() != null) {
            userResponse.setName(user.getPatient().getName());
            userResponse.setPhone(user.getPatient().getPhone());
            userResponse.setAddress(user.getPatient().getAddress());
            userResponse.setDob(user.getPatient().getDob());
            userResponse.setRole(user.getRole().name());
        }

        return userResponse;
    }

}
