package az.hamburg.it.hamburg.academy.website.model.request.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class UpdateContactRequest {
    @Size(max = 1024)
    String address;

    @Size(max = 1024)
    String mapUrl;

    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.(com|ru)$", message = "Email must end with .com or .ru")
    String email;

    @Pattern(
            regexp = "^\\+994(50|51|55|70|77|99)[0-9]{7}$",
            message = "Phone number must start with +994 and a valid operator code"
    )
    String phoneNumber;

    @Size(max = 1024)
    String linkedinUrl;

    @Size(max = 256)
    String instagramUrl;

    @Size(max = 256)
    String tiktokUrl;
}