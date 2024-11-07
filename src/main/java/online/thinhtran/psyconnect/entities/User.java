    package online.thinhtran.psyconnect.entities;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import com.fasterxml.jackson.core.JsonParser;
    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.databind.DeserializationContext;
    import com.fasterxml.jackson.databind.JsonDeserializer;
    import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Size;
    import lombok.*;
    import online.thinhtran.psyconnect.common.RoleEnum;
    import online.thinhtran.psyconnect.common.StatusEnum;
    import org.hibernate.annotations.ColumnDefault;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.annotation.LastModifiedDate;
    import org.springframework.data.jpa.domain.support.AuditingEntityListener;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsChecker;

    import java.io.IOException;
    import java.time.Instant;
    import java.time.LocalDateTime;
    import java.util.Collection;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    @Getter
    @Setter
    @Entity
    @Table(name = "Users", schema = "psy")
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EntityListeners(AuditingEntityListener.class)
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class User implements UserDetails {
        @Id
        @Column(name = "user_id", nullable = false)
        private Integer id;

        @Size(max = 255)
        @NotNull
        @Column(name = "username", nullable = false)
        private String username;

        @Size(max = 255)
        @NotNull
        @Column(name = "email", nullable = false)
        private String email;

        @Size(max = 255)
        @NotNull
        @Column(name = "password", nullable = false)
        private String password;

        @NotNull
        @Column(name = "role", nullable = false)
        @Enumerated(EnumType.STRING)
        private RoleEnum role;

        @Column(name = "avatar")
        private String avatar;

        @Enumerated(EnumType.STRING)
        private StatusEnum status;

    //    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    //    private Doctor doctor;
    //
    //    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    //    private Patient patient;

        @CreatedDate
        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @LastModifiedDate
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Override
        @JsonIgnore
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_" + role.name().toUpperCase()));
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return UserDetails.super.isAccountNonExpired();
        }

        @Override
        public boolean isAccountNonLocked() {
            return UserDetails.super.isAccountNonLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return UserDetails.super.isCredentialsNonExpired();
        }

        @Override
        public boolean isEnabled() {
            return this.status == StatusEnum.APPROVED;
        }

        @PrePersist
        protected void onCreate() {
            this.id = (int) (Math.random() * 1000000);

        }


    }

