package online.thinhtran.psyconnect.responses.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import online.thinhtran.psyconnect.entities.User;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String username;
    private String name;
    private String email;
    private String phone;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String specialization;
    private String role;

    public static UserResponse fromEntity(User user) {
        UserResponse userResponse = UserResponse.builder()
                .username(user.getUsername())
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
