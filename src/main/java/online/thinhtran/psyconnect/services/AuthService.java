package online.thinhtran.psyconnect.services;

import com.cloudinary.api.exceptions.BadRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.common.RoleEnum;
import online.thinhtran.psyconnect.common.StatusEnum;
import online.thinhtran.psyconnect.components.JwtGenerator;
import online.thinhtran.psyconnect.dto.auth.ChangePasswordDto;
import online.thinhtran.psyconnect.dto.auth.LoginDto;
import online.thinhtran.psyconnect.dto.auth.RegisterDto;
import online.thinhtran.psyconnect.dto.mail.MailDto;
import online.thinhtran.psyconnect.entities.Doctor;
import online.thinhtran.psyconnect.entities.Patient;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.exceptions.ResourceAlreadyExisted;
import online.thinhtran.psyconnect.exceptions.ResourceNotFound;
import online.thinhtran.psyconnect.repositories.DoctorRepository;
import online.thinhtran.psyconnect.repositories.PatientRepository;
import online.thinhtran.psyconnect.repositories.UserRepository;
import online.thinhtran.psyconnect.responses.auth.DoctorRegisterResponse;
import online.thinhtran.psyconnect.responses.auth.LoginResponse;
import online.thinhtran.psyconnect.responses.auth.PatientRegisterResponse;
import online.thinhtran.psyconnect.responses.auth.RegisterResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final JwtGenerator jwtGenerator;
    private final GrantedTokenService grantedTokenService;
    private final MailService mailService;

    @Transactional
    @CacheEvict(value = {"users", "doctors"}, allEntries = true)
    public RegisterResponse register(RegisterDto registerDto) {
        if (registerDto.getRole().equalsIgnoreCase(RoleEnum.DOCTOR.name())) {
            return registerDoctor(registerDto);
        }

        if (registerDto.getRole().equalsIgnoreCase(RoleEnum.PATIENT.name())) {
            return registerPatient(registerDto);
        }
        return null;

    }

    private PatientRegisterResponse registerPatient(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ResourceAlreadyExisted("Patient already existed with this username");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ResourceAlreadyExisted("Patient already existed with this email");
        }

        if (patientRepository.existsByPhone(registerDto.getPhone())) {
            throw new ResourceAlreadyExisted("Patient already existed with this phone");
        }

        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(RoleEnum.PATIENT)
                .status(StatusEnum.APPROVED)
                .build();

        Patient patient = Patient.builder()
                .user(user)
                .name(registerDto.getName())
                .phone(registerDto.getPhone())
                .dob(registerDto.getDob())
                .address(registerDto.getAddress())
                .build();

        patientRepository.save(patient);

        return PatientRegisterResponse.builder()

                .username(user.getUsername())
                .name(patient.getName())
                .address(patient.getAddress())
                .email(user.getEmail())
                .phone(patient.getPhone())
                .dob(patient.getDob())
                .build();
    }

    private DoctorRegisterResponse registerDoctor(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ResourceAlreadyExisted("Doctor already existed with this username");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ResourceAlreadyExisted("Doctor already existed with this email");
        }

        if (doctorRepository.existsByPhone(registerDto.getPhone())) {
            throw new ResourceAlreadyExisted("Doctor already existed with this phone");
        }

        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(RoleEnum.DOCTOR)
                .status(StatusEnum.PENDING)
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

    @Transactional(readOnly = true)
    public LoginResponse login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new BadCredentialsException("User not existed"));

        String jwtToken = authenticateUser(loginDto, user);

        if (loginDto.getRole().equalsIgnoreCase(RoleEnum.DOCTOR.name())) {
            Doctor doctor = doctorRepository.findByUser_Username(user.getUsername()).orElseThrow();
            return LoginResponse.builder()
                    .id(doctor.getId())
                    .username(user.getUsername())
                    .name(doctor.getName())
                    .email(user.getEmail())
                    .phone(doctor.getPhone())
                    .address(doctor.getAddress())
                    .dob(doctor.getDob())
                    .specialization(doctor.getSpecialization())
                    .token(jwtToken)
                    .role(user.getRole().name())
                    .build();
        } else {
            Patient patient = patientRepository.findByUser_Username(user.getUsername()).orElseThrow();
            return LoginResponse.builder()
                    .id(patient.getId())
                    .username(user.getUsername())
                    .name(patient.getName())
                    .email(user.getEmail())
                    .phone(patient.getPhone())
                    .address(patient.getAddress())
                    .dob(patient.getDob())
                    .token(jwtToken)
                    .role(user.getRole().name())
                    .build();
        }
    }


    @Transactional
    public LoginResponse adminLogin(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new BadCredentialsException("User not existed"));

        if (user.getRole() != RoleEnum.ADMIN) {
            throw new BadCredentialsException("Invalid request");
        }

        String jwtToken = authenticateUser(loginDto, user);

        return LoginResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .token(jwtToken)
                .role(user.getRole().name())
                .build();
    }

    private String authenticateUser(LoginDto loginDto, User user) {
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword(),
                user.getAuthorities()
        );

        authenticationManager.authenticate(authenticationToken);

        String jwtToken = jwtGenerator.generateToken(user);
        grantedTokenService.grantToken(jwtToken);

        return jwtToken;
    }

    @Transactional
    public void approveDoctor(Integer id) throws BadRequest {
        Doctor doctor = doctorRepository.findByUser_Id(id)
                .orElseThrow(() -> new ResourceNotFound("Doctor not found"));

        if (doctor.getUser().getRole() == RoleEnum.DOCTOR && doctor.getUser().getStatus() == StatusEnum.PENDING) {
            doctor.getUser().setStatus(StatusEnum.APPROVED);
            doctorRepository.save(doctor);
        } else {
            throw new BadRequest("Invalid request");
        }
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true) //todo: just update cache for user
    public void changePassword(User user, ChangePasswordDto changePasswordDto) {
        log.info("Change pass dto: {}", changePasswordDto);

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void forgotPassword(MailDto mailDto) throws MessagingException, IOException {
        mailDto.setContent(UUID.randomUUID().toString().substring(0, 8));
        mailDto.setSubject("Change password");

        User user = userRepository.findByEmail(mailDto.getTo())
                .orElseThrow(() -> new BadCredentialsException("User not existed"));

        user.setPassword(passwordEncoder.encode(mailDto.getContent()));

        mailService.sendMail(mailDto);
    }
}
