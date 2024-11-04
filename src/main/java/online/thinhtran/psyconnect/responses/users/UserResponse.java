package online.thinhtran.psyconnect.responses.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String role;


}
