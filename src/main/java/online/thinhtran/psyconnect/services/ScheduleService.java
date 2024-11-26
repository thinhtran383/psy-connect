package online.thinhtran.psyconnect.services;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.common.StatusEnum;
import online.thinhtran.psyconnect.dto.schedule.ScheduleDto;
import online.thinhtran.psyconnect.entities.Schedule;
import online.thinhtran.psyconnect.repositories.ScheduleRepository;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.schedule.ScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void createSchedule(ScheduleDto scheduleDto, Integer patientId) {
        Schedule schedule = Schedule.builder()
                .appointmentDate(scheduleDto.getAppointmentDate())
                .doctor(scheduleDto.getDoctorId())
                .patient(patientId)
                .notes(scheduleDto.getNotes())
                .status(StatusEnum.PENDING)
                .build();
        scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public PageableResponse<ScheduleResponse> getAllScheduleByDoctorId(Integer doctorId, int page, int size) {
        Page<ScheduleResponse> schedules = scheduleRepository.findAllByDoctorId(doctorId, PageRequest.of(page, size));

        return new PageableResponse<>(schedules.getContent(), schedules.getTotalElements(), schedules.getTotalPages());
    }

    @Transactional(readOnly = true)
    public PageableResponse<ScheduleResponse> getAllScheduleByPatientId(Integer patientId, int page, int size) {
        Page<ScheduleResponse> schedules = scheduleRepository.findAllByPatientId(patientId, PageRequest.of(page, size));

        return new PageableResponse<>(schedules.getContent(), schedules.getTotalElements(), schedules.getTotalPages());
    }

    public void updateScheduleStatus(Integer scheduleId, StatusEnum status) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        schedule.setStatus(status);
        scheduleRepository.save(schedule);
    }
}
