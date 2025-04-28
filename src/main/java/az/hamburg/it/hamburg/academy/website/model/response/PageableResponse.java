package az.hamburg.it.hamburg.academy.website.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class PageableResponse<T> {
    List<T> content;
    int lastPageNumber;
    long totalElements;
    boolean hasNextPage;
}
