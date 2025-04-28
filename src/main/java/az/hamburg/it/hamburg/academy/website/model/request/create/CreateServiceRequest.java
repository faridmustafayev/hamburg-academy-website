package az.hamburg.it.hamburg.academy.website.model.request.create;

import az.hamburg.it.hamburg.academy.website.model.enums.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreateServiceRequest {
    @NotNull(message = "Service type can't be null")
    ServiceType name;

    @NotBlank(message = "description can't be blank")
    String description;
}
