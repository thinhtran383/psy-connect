package online.thinhtran.psyconnect.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import online.thinhtran.psyconnect.common.StatusEnum;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Schedules", schema = "psy")
@EntityListeners(AuditingEntityListener.class)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    private Integer doctor;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    private Integer patient;

    @NotNull
    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Lob
    @Column(name = "notes")
    private String notes;

}