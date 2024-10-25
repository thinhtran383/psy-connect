package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByUser_Username(String username);

    Optional<Doctor> findByUser_Id(Integer id);
}