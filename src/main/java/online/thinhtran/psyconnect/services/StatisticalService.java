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
        return doctorRepository.findTop5ByOrderByRatingDesc();
    }
}
