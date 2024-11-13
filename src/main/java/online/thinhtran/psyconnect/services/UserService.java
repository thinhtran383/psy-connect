package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.common.RoleEnum;
import online.thinhtran.psyconnect.entities.Doctor;
import online.thinhtran.psyconnect.entities.Patient;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.exceptions.ResourceNotFound;
import online.thinhtran.psyconnect.repositories.DoctorRepository;
import online.thinhtran.psyconnect.repositories.PatientRepository;
import online.thinhtran.psyconnect.repositories.UserRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.users.UserDetailResponse;
import online.thinhtran.psyconnect.responses.users.UserResponse;
import online.thinhtran.psyconnect.responses.users.doctor.DoctorDetailResponse;
import online.thinhtran.psyconnect.responses.users.doctor.DoctorInfoResponse;
import online.thinhtran.psyconnect.responses.users.patient.PatientDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
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
        return userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#page + '_' + #size + '_' + #roleEnum")
    public PageableResponse<UserResponse> getAllUsers(int page, int size, RoleEnum roleEnum) {
        Page<User> users;
        if (roleEnum != null) {
            users = userRepository.findAllByRole(roleEnum, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        } else {
            users = userRepository.findAllByRoleNot(RoleEnum.ADMIN, PageRequest.of(page, size, Sort.by("createdAt").descending()));
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
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found"));


        if (user != null && user.getRole().equals(RoleEnum.DOCTOR)) {
            Doctor doctor = doctorRepository.findByUser_Id(userId).orElse(null);

            return DoctorDetailResponse.builder()
                    .id(user.getId())
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

        if (user != null && user.getRole().equals(RoleEnum.PATIENT)) {
            Patient patient = patientRepository.findByUser_Id(userId).orElse(null);

            return PatientDetailResponse.builder()
                    .id(user.getId())
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
    @CacheEvict(value = "doctors", allEntries = true)
    public void updateRating(Integer doctorId, Float rating) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ResourceNotFound("Doctor not found"));
        doctor.setRating(rating);

        doctorRepository.save(doctor);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "doctors", key = "#page + '_' + #size + '_' + #specialization")
    public PageableResponse<DoctorInfoResponse> getAllDoctorCatalog(String specialization, int page, int size) {
//        Page<DoctorInfoResponse> doctors = doctorRepository.findAllDoctor(PageRequest.of(page, size));
//
//        List<DoctorInfoResponse> filteredDoctors = specialization == null
//                ? doctors.getContent()
//                : doctors.getContent().stream()
//                .filter(doctor -> specialization.equalsIgnoreCase(doctor.getSpecialization()))
//                .collect(Collectors.toList());

//        return PageableResponse.<DoctorInfoResponse>builder()
//                .elements(filteredDoctors)
//                .totalElements(filteredDoctors.size())
//                .totalPages((int) Math.ceil((double) filteredDoctors.size() / size))
//                .build();
        Page<DoctorInfoResponse> doctors;

        if (specialization == null) {
            doctors = doctorRepository.findAllDoctor(PageRequest.of(page, size));
        } else {
            doctors = doctorRepository.findDoctorsBySpecialization(specialization, PageRequest.of(page, size));
        }

        return PageableResponse.<DoctorInfoResponse>builder()
                .elements(doctors.getContent())
                .totalElements(doctors.getTotalElements())
                .totalPages(doctors.getTotalPages())
                .build();

    }


    @Transactional(readOnly = true)
    protected Doctor getUserIdByDoctorId(Integer doctorId) { // todo: rename
        return doctorRepository.findByUser_Id(doctorId).orElseThrow(() -> new ResourceNotFound("Doctor not found"));
    }

    @Transactional(readOnly = true)
    public PageableResponse<String> getAllSpecialization(int page, int size) {
        Page<String> specializations = doctorRepository.findAllSpecialization(PageRequest.of(page, size));
        return PageableResponse.<String>builder()
                .elements(specializations.getContent())
                .totalElements(specializations.getTotalElements())
                .totalPages(specializations.getTotalPages())
                .build();
    }

}
