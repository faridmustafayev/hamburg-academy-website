package az.hamburg.it.hamburg.academy.website.model.criteria;

import az.hamburg.it.hamburg.academy.website.model.enums.SpecializationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GraduateCriteria {
    String fullName;
    String linkedin;
    SpecializationType category;
}
