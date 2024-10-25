package online.thinhtran.psyconnect.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "rating")
    private Float rating;

    @Column(name = "total_ratings")
    private Integer totalRatings;

    @Column(name = "successful_sessions")
    private Integer successfulSessions;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;


    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = (int) (Math.random() * 1000000);
        }


        if (this.rating == null) {
            this.rating = 0.0f;
        }
        if (this.totalRatings == null) {
            this.totalRatings = 0;
        }
        if (this.successfulSessions == null) {
            this.successfulSessions = 0;
        }

    }
}