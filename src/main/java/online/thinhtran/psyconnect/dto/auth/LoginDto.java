package online.thinhtran.psyconnect.dto.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {
    private String username;
    private String password;
    private String role;
}
