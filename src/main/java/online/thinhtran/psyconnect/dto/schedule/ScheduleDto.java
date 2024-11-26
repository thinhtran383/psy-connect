package online.thinhtran.psyconnect.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDto {
    private Integer doctorId;
    private String notes;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime appointmentDate;
}
