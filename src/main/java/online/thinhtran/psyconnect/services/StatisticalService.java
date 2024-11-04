package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.repositories.DoctorRepository;
import online.thinhtran.psyconnect.repositories.PatientRepository;
import online.thinhtran.psyconnect.responses.statistical.DoctorAndPatientResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
