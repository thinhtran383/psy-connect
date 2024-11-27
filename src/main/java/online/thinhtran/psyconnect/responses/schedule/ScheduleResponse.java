package online.thinhtran.psyconnect.responses.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import online.thinhtran.psyconnect.common.StatusEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleResponse {
    private Integer id;
    private Integer userId;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime appointmentDate;
    private String notes;
    private StatusEnum status;
    private String phone;

}
