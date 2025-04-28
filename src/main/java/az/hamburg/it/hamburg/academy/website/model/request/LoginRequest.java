package az.hamburg.it.hamburg.academy.website.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class LoginRequest {
    @NotBlank(message = "email can't be blank")
    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.(com|ru)$", message = "Email must end with .com or .ru")
    String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    String password;
}