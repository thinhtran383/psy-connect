package online.thinhtran.psyconnect.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Certificates", schema = "psy")
public class Certificate {
    @Id
    @Column(name = "certificate_id", nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Size(max = 255)
    @Column(name = "certificate_name")
    private String certificateName;

    @Size(max = 255)
    @Column(name = "certificate_image")
    private String certificateImage;

    @PrePersist
    public void prePersist() {
        this.id = (int) (Math.random() * 1000000);
    }

}