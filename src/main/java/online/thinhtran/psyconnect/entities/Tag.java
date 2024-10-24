package online.thinhtran.psyconnect.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Tags", schema = "psy")
public class Tag {
    @Id
    @Column(name = "tag_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "tag_name", nullable = false, length = 100)
    private String tagName;

}