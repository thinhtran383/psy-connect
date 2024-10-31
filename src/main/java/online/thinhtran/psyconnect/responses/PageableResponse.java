package online.thinhtran.psyconnect.responses;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse<T> {
    private List<T> elements;
    private long totalPages;
    private long totalElements;
}
