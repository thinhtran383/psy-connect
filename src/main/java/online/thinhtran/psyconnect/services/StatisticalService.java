package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.entities.Doctor;
import online.thinhtran.psyconnect.repositories.DoctorRepository;
import online.thinhtran.psyconnect.repositories.PatientRepository;
import online.thinhtran.psyconnect.responses.statistical.DoctorAndPatientResponse;
import online.thinhtran.psyconnect.responses.users.doctor.DoctorInfoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticalService {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public DoctorAndPatientResponse getDoctorAndPatient() {
        return DoctorAndPatientResponse.builder()
                .doctor(doctorRepository.count())
                .patient(patientRepository.count())
                .build();
    }

    @Transactional(readOnly = true)
    public List<DoctorInfoResponse> top5Doctor() {
        List<Doctor> doctors = doctorRepository.findTop5ByOrderByRatingDesc();
        return doctors.stream()
                .map(doctor -> DoctorInfoResponse.builder()
                        .id(doctor.getUser().getId())
                        .name(doctor.getName())
                        .avatar(doctor.getUser().getAvatar())
                        .specialization(doctor.getSpecialization())
                        .avgRating(doctor.getRating())
                        .build())
                .collect(Collectors.toList());
    }
}
