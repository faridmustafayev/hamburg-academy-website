package az.hamburg.it.hamburg.academy.website.model.request.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreateProjectRequestInput {
    @NotBlank(message = "Full name can not be blank")
    @Size(max = 128, message = "Full name can't exceed 128 characters")
    String fullName;

    @NotBlank(message = "email can't be blank")
    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.(com|ru)$", message = "Email must end with .com or .ru")
    String email;

    @NotBlank(message = "phone number can't be blank")
    @Pattern(
            regexp = "^\\+994(50|51|55|70|77|99)[0-9]{7}$",
            message = "Phone number must start with +994 and a valid operator code"
    )
    String phoneNumber;

    @NotEmpty(message = "At least one service must be selected")
    @Size(max = 2, message = "You can select up to 2 services")
    List<@NotNull(message = "Specialization id must not be null") Long> serviceIds;
}