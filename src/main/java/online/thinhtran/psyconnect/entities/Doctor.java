package online.thinhtran.psyconnect.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Doctors", schema = "psy")
public class Doctor {
    @Id
    @Column(name = "doctor_id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "address")
    private String address;

    @Size(max = 255)
    @Column(name = "phone")
    private String phone;

    @Column(name = "dob")
    private LocalDate dob;

    @Size(max = 255)
    @NotNull
    @Column(name = "specialization", nullable = false)
    private String specialization;

    @NotNull
    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;

    @ColumnDefault("0")
    @Column(name = "rating")
    private Float rating;

    @ColumnDefault("0")
    @Column(name = "total_ratings")
    private Integer totalRatings;

    @ColumnDefault("0")
    @Column(name = "successful_sessions")
    private Integer successfulSessions;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;


    @PrePersist
    protected void onCreate() {
        this.id = (int) (Math.random() * 1000000);
    }
}