package online.thinhtran.psyconnect.dto.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;
}
