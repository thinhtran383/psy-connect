package online.thinhtran.psyconnect.services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.common.StatusEnum;
import online.thinhtran.psyconnect.dto.mail.MailDto;
import online.thinhtran.psyconnect.dto.schedule.ScheduleDto;
import online.thinhtran.psyconnect.entities.Doctor;
import online.thinhtran.psyconnect.entities.Patient;
import online.thinhtran.psyconnect.entities.Schedule;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.repositories.ScheduleRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.schedule.ScheduleResponse;
import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MailService mailService;
    private final UserService userService;
    private final ScheduledTasksEndpoint scheduledTasksEndpoint;

    @Transactional
    public void createSchedule(ScheduleDto scheduleDto, Integer patientId) throws MessagingException {
        Schedule schedule = Schedule.builder()
                .appointmentDate(scheduleDto.getAppointmentDate())
                .doctor(scheduleDto.getDoctorId())
                .patient(patientId)
                .notes(scheduleDto.getNotes())
                .status(StatusEnum.PENDING)
                .build();

        Doctor doctor = userService.findDoctorByUserId(scheduleDto.getDoctorId());
        Patient patient = userService.findPatientByUserId(patientId);

        LocalDate date = scheduleDto.getAppointmentDate().toLocalDate();
        LocalTime time = scheduleDto.getAppointmentDate().toLocalTime();

        MailDto mailDto = MailDto.builder()
                .subject("New Schedule")
                .to(doctor.getUser().getEmail())
                .templateName("schedule_request")
                .placeholders(Map.of(
                        "doctorName", doctor.getName(),
                        "patientName", patient.getName(),
                        "appointmentDate", date.toString(),
                        "appointmentTime", time.toString(),
                        "appointmentDetails", scheduleDto.getNotes()

                ))
                .build();

        mailService.sendMail(mailDto);

        scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public PageableResponse<ScheduleResponse> getAllScheduleByDoctorId(Integer doctorId, StatusEnum statusEnum, int page, int size) {
        Page<ScheduleResponse> schedules = scheduleRepository.findAllByDoctorId(doctorId, statusEnum, PageRequest.of(page, size));

        return new PageableResponse<>(schedules.getContent(), schedules.getTotalElements(), schedules.getTotalPages());
    }

    @Transactional(readOnly = true)
    public PageableResponse<ScheduleResponse> getAllScheduleByPatientId(Integer patientId, StatusEnum statusEnum, int page, int size) {
        Page<ScheduleResponse> schedules = scheduleRepository.findAllByPatientId(patientId, statusEnum, PageRequest.of(page, size));

        return new PageableResponse<>(schedules.getContent(), schedules.getTotalElements(), schedules.getTotalPages());
    }

    @Transactional
    public void updateScheduleStatus(Integer scheduleId, StatusEnum status) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        schedule.setStatus(status);
        scheduleRepository.save(schedule);
    }

}
