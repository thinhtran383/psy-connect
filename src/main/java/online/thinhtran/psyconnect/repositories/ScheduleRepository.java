package online.thinhtran.psyconnect.repositories;

import online.thinhtran.psyconnect.entities.Schedule;
import online.thinhtran.psyconnect.responses.schedule.ScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    @Query("""
                select new online.thinhtran.psyconnect.responses.schedule.ScheduleResponse(
                 s.id, p.name, s.appointmentDate, s.notes, s.status, p.phone) from Schedule s
                join User sender on s.patient = sender.id
                join Patient p on sender.id = p.user.id
                where s.doctor = :doctorId and s.status <> 'CANCELLED'
            """)
    Page<ScheduleResponse> findAllByDoctorId(Integer doctorId, Pageable pageable);


    @Query("""
                select new online.thinhtran.psyconnect.responses.schedule.ScheduleResponse(
                 s.id, d.name, s.appointmentDate, s.notes, s.status, d.phone) from Schedule s
                join User sender on s.patient = sender.id
                join Doctor d on sender.id = d.user.id
                where s.patient = :patientId
            """)
    Page<ScheduleResponse> findAllByPatientId(Integer patientId, Pageable pageable);



}