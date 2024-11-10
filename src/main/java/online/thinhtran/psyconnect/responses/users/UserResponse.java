package online.thinhtran.psyconnect.responses.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import online.thinhtran.psyconnect.common.StatusEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String role;
    private String status;


}
