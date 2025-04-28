package az.hamburg.it.hamburg.academy.website.model.request.update;

import jakarta.validation.constraints.Email;
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
public class UpdateProjectRequestInput {
    @Size(max = 128, message = "Full name can't exceed 128 characters")
    String fullName;

    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.(com|ru)$", message = "Email must end with .com or .ru")
    String email;

    @Pattern(
            regexp = "^\\+994(50|51|55|70|77|99)[0-9]{7}$",
            message = "Phone number must start with +994 and a valid operator code"
    )
    String phoneNumber;

    @Size(max = 2, message = "You can select up to 2 specializations")
    List<Long> serviceIds;
}