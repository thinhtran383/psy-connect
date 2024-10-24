package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.common.RoleEnum;
import online.thinhtran.psyconnect.dto.RegisterDto;
import online.thinhtran.psyconnect.entities.Doctor;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.exceptions.ResourceAlreadyExisted;
import online.thinhtran.psyconnect.repositories.DoctorRepository;
import online.thinhtran.psyconnect.repositories.UserRepository;
import online.thinhtran.psyconnect.responses.Auth.DoctorRegisterResponse;
import online.thinhtran.psyconnect.responses.Auth.RegisterResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final DoctorRepository doctorRepository;

    @Transactional
    public RegisterResponse register(RegisterDto registerDto) {
        if (registerDto.getRole().equalsIgnoreCase(RoleEnum.DOCTOR.name())) {
            return registerDoctor(registerDto);
        }

        return null;

    }

    private DoctorRegisterResponse registerDoctor(RegisterDto registerDto) {
        Doctor existingDoctor = doctorRepository.findByUser_Username(registerDto.getUsername()).orElse(null);

        if (existingDoctor != null) {
            if (existingDoctor.getUser().getEmail() != null && existingDoctor.getUser().getEmail().equals(registerDto.getEmail())) {
                throw new ResourceAlreadyExisted("Doctor already existed with this email");
            }

            if (existingDoctor.getPhone() != null && existingDoctor.getPhone().equals(registerDto.getPhone())) {
                throw new ResourceAlreadyExisted("Doctor already existed with this phone");
            }


            throw new ResourceAlreadyExisted("Doctor already existed with this username");
        }

        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(RoleEnum.DOCTOR)
                .build();


        Doctor doctor = Doctor.builder()
                .user(user)
                .name(registerDto.getName())
                .phone(registerDto.getPhone())
                .dob(registerDto.getDob())
                .address(registerDto.getAddress())
                .specialization(registerDto.getSpecialization())
                .experienceYears(registerDto.getExperienceYears())
                .build();

        doctorRepository.save(doctor);

        return DoctorRegisterResponse.builder()
                .username(user.getUsername())
                .name(doctor.getName())
                .address(doctor.getAddress())
                .email(user.getEmail())
                .phone(doctor.getPhone())
                .dob(doctor.getDob())
                .specialization(doctor.getSpecialization())
                .build();

    }


}
