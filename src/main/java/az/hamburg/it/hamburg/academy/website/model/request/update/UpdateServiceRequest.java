package az.hamburg.it.hamburg.academy.website.model.request.update;

import az.hamburg.it.hamburg.academy.website.model.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UpdateServiceRequest {
    ServiceType name;
    String description;
}
