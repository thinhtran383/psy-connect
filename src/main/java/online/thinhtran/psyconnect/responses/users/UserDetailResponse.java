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

    public static UserDetailResponse fromEntity(User user) {
        UserDetailResponse userResponse = UserDetailResponse.builder()
                .email(user.getEmail())
                .build();

        if (user.getDoctor() != null) {
            Doctor doctor = user.getDoctor();

            userResponse.setName(doctor.getName());
            userResponse.setPhone(doctor.getPhone());
            userResponse.setAddress(doctor.getAddress());
            userResponse.setDob(doctor.getDob());
            userResponse.setSpecialization(doctor.getSpecialization());
            userResponse.setRole(user.getRole().name());
            userResponse.setCreatedDate(LocalDateTime.from(doctor.getCreatedAt()));
            userResponse.setLastModifiedDate(LocalDateTime.from(doctor.getUpdatedAt()));

        } else if (user.getPatient() != null) {
            Patient patient = user.getPatient();

            userResponse.setName(patient.getName());
            userResponse.setPhone(patient.getPhone());
            userResponse.setAddress(patient.getAddress());
            userResponse.setDob(patient.getDob());
            userResponse.setRole(user.getRole().name());
            userResponse.setCreatedDate(LocalDateTime.from(patient.getCreatedAt()));
            userResponse.setLastModifiedDate(LocalDateTime.from(patient.getUpdatedAt()));
        }

        return userResponse;
    }

}
