package az.hamburg.it.hamburg.academy.website.model.criteria;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.USE_DEFAULTS;
import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(USE_DEFAULTS)
@FieldDefaults(level = PRIVATE)
public class PageCriteria {
    Integer page = 0;
    Integer count = 10;
}
