package online.thinhtran.psyconnect.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> {
    private String message;
    private T data;
}
