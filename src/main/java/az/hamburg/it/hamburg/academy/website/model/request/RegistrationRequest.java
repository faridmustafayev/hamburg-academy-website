package az.hamburg.it.hamburg.academy.website.model.request;

import az.hamburg.it.hamburg.academy.website.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = PRIVATE)
public class RegistrationRequest {
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 255, message = "First name cannot be longer than 255 characters")
    String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 255, message = "Last name cannot be longer than 255 characters")
    String lastName;

    @NotNull(message = "Age cannot be null")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 150, message = "Age must be less than 150")
    Integer age;

    @NotBlank(message = "email can't be blank")
    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.(com|ru)$", message = "The email must also have the @ symbol and end with .com or .ru.")
    String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    String password;

    @NotNull(message = "Role cannot be null")
    Role role;
}