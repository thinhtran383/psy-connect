package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Doctor;
import online.thinhtran.psyconnect.responses.users.doctor.DoctorInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByUser_Username(String username);

    Optional<Doctor> findByUser_Id(Integer id);

    boolean existsByPhone(String phone);


    @Query("""
                select new online.thinhtran.psyconnect.responses.users.doctor.DoctorInfoResponse(
                    d.user.id,
                    d.name,
                    d.user.avatar,
                    d.specialization,
                    d.rating
                ) from Doctor d
            
            """)
    Page<DoctorInfoResponse> findAllDoctor(Pageable pageable);

    @Query("""
                select d.specialization from Doctor d
            """)
    Page<String> findAllSpecialization(Pageable pageable);


    @Query("""
                select new online.thinhtran.psyconnect.responses.users.doctor.DoctorInfoResponse(
                    d.user.id,
                    d.name,
                    d.user.avatar,
                    d.specialization,
                    d.rating
                ) from Doctor d where LOWER(d.specialization) = LOWER(:specialization)
            """)
    Page<DoctorInfoResponse> findDoctorsBySpecialization(String specialization, Pageable pageable);


    @Query("""
                select new online.thinhtran.psyconnect.responses.users.doctor.DoctorInfoResponse(
                    d.user.id,
                    d.name,
                    d.user.avatar,
                    d.specialization,
                    d.rating
                ) from Doctor d order by d.rating desc limit 5
            """)
    List<DoctorInfoResponse> findTop5ByOrderByRatingDesc();
}