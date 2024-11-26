package online.thinhtran.psyconnect.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.thinhtran.psyconnect.common.StatusEnum;
import online.thinhtran.psyconnect.dto.schedule.ScheduleDto;
import online.thinhtran.psyconnect.entities.User;
import online.thinhtran.psyconnect.responses.PageableResponse;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.schedule.ScheduleResponse;
import online.thinhtran.psyconnect.services.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-path}/schedules")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Response<?>> createAppointment(
            ScheduleDto scheduleDto,
            @AuthenticationPrincipal User user
    ) {
        scheduleService.createSchedule(scheduleDto, user.getId());
        return ResponseEntity.ok(
                Response.builder()
                        .data(null)
                        .message("Appointment created successfully")
                        .build()
        );
    }

    @GetMapping("/patient")
    public ResponseEntity<Response<PageableResponse<ScheduleResponse>>> getScheduleByPatientId(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        log.info("Getting schedules for patient: {}", user.getId());

        return ResponseEntity.ok(
                Response.<PageableResponse<ScheduleResponse>>builder()
                        .data(scheduleService.getAllScheduleByPatientId(user.getId(), page, size))
                        .message("Schedules retrieved successfully")
                        .build()
        );
    }

    @GetMapping("/doctor")
    public ResponseEntity<Response<PageableResponse<ScheduleResponse>>> getScheduleByDoctorId(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                Response.<PageableResponse<ScheduleResponse>>builder()
                        .data(scheduleService.getAllScheduleByDoctorId(user.getId(), page, size))
                        .message("Schedules retrieved successfully")
                        .build()
        );
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<Response<?>> updateScheduleStatus(
            @PathVariable Integer scheduleId,
            @RequestParam StatusEnum status
    ) {
        scheduleService.updateScheduleStatus(scheduleId, status);
        return ResponseEntity.ok(
                Response.builder()
                        .data(null)
                        .message("Schedule status updated successfully")
                        .build()
        );
    }

}
