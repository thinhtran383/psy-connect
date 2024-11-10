package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import online.thinhtran.psyconnect.responses.users.doctor.DoctorDetailResponse;
import online.thinhtran.psyconnect.responses.users.patient.PatientDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final CertificateService certificateService;

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#username")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#page + '_' + #size + '_' + #roleEnum")
    public PageableResponse<UserResponse> getAllUsers(int page, int size, RoleEnum roleEnum) {
        Page<User> users;
        if (roleEnum != null) {
            users = userRepository.findAllByRole(roleEnum, PageRequest.of(page, size));
        } else {
            users = userRepository.findAllByRoleNot(RoleEnum.ADMIN, PageRequest.of(page, size));
        }

        return PageableResponse.<UserResponse>builder()
                .elements(users.stream()
                        .map(user -> modelMapper.map(user, UserResponse.class))
                        .collect(Collectors.toList()))
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = doctorRepository.findByUser_Id(userId).orElse(null);
        Patient patient = patientRepository.findByUser_Id(userId).orElse(null);

        if (doctor != null) {
            return DoctorDetailResponse.builder()
                    .id(doctor.getId())
                    .about(doctor.getAbout())
                    .degree(doctor.getDegree())
                    .avatar(user.getAvatar())
                    .name(doctor.getName())
                    .email(user.getEmail())
                    .phone(doctor.getPhone())
                    .dob(doctor.getDob())
                    .specialization(doctor.getSpecialization())
                    .avatar(user.getAvatar())
                    .address(doctor.getAddress())
                    .role(user.getRole().name())
                    .createdDate(user.getCreatedAt())
                    .avgRating(doctor.getRating())
                    .lastModifiedDate(user.getUpdatedAt())
                    .experience(doctor.getExperienceYears())
                    .certificates(certificateService.getCertificateImages(user.getId()))
                    .build();
        }

        if (patient != null) {
            return PatientDetailResponse.builder()
                    .id(patient.getId())
                    .name(patient.getName())
                    .email(user.getEmail())
                    .phone(patient.getPhone())
                    .dob(patient.getDob())

                    .role(user.getRole().name())
                    .address(patient.getAddress())
                    .createdDate(user.getCreatedAt())
                    .lastModifiedDate(user.getUpdatedAt())
                    .build();
        }

        return null;
    }

    @Transactional
    public void updateRating(Integer userId, Float rating) {
        Doctor doctor = doctorRepository.findById(userId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setRating(rating);

        doctorRepository.save(doctor);
    }


}
