package online.thinhtran.psyconnect.responses.tag;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponse {
    private Integer id;
    private String tagName;
}
