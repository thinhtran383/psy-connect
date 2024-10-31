package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.common.RoleEnum;
import online.thinhtran.psyconnect.entities.Doctor;
import online.thinhtran.psyconnect.entities.Patient;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.DoctorRepository;
import online.thinhtran.psyconnect.repositories.PatientRepository;
import online.thinhtran.psyconnect.repositories.UserRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.users.UserDetailResponse;
import online.thinhtran.psyconnect.responses.users.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#page + '_' + #size + '_' + #roleEnum")
    public PageableResponse<UserResponse> getAllUsers(int page, int size, RoleEnum roleEnum) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));

        List<User> content = users.getContent();

        if (roleEnum != null) {
            content = content.stream()
                    .filter(user -> user.getRole().equals(roleEnum))
                    .toList();
        }

        // calculate total pages and total elements
        int totalElements = content.size();
        int totalPages = (totalElements + size - 1) / size;

        return PageableResponse.<UserResponse>builder()
                .elements(content.stream()
                        .map(user -> modelMapper.map(user, UserResponse.class))
                        .collect(Collectors.toList()))
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = doctorRepository.findByUser_Username(username).orElse(null);
        Patient patient = patientRepository.findByUser_Username(username).orElse(null);

        if (doctor != null) {
            return UserDetailResponse.builder()
                    .name(doctor.getName())
                    .email(user.getEmail())
                    .phone(doctor.getPhone())
                    .dob(doctor.getDob())
                    .specialization(doctor.getSpecialization())
                    .role(user.getRole().name())
                    .createdDate(user.getCreatedAt())
                    .lastModifiedDate(user.getUpdatedAt())
                    .build();
        }

        if (patient != null) {
            return UserDetailResponse.builder()
                    .name(patient.getName())
                    .email(user.getEmail())
                    .phone(patient.getPhone())
                    .dob(patient.getDob())
                    .role(user.getRole().name())
                    .createdDate(user.getCreatedAt())
                    .lastModifiedDate(user.getUpdatedAt())
                    .build();
        }

        return null;
    }

}
