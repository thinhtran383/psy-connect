package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByUser_Username(String username);

    Optional<Patient> findByUser_Id(Integer id);

    boolean existsByPhone(String phone);
}