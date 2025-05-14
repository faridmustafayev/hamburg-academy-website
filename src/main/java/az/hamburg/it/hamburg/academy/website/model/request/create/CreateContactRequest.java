package az.hamburg.it.hamburg.academy.website.model.request.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Builder
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreateContactRequest {
    @NotBlank(message = "address can not be blank")
    @Size(max = 1024)
    String address;

    @NotBlank(message = "map url can not be blank")
    @Size(max = 1024)
    String mapUrl;

    @NotBlank(message = "email can't be blank")
    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.(com|ru)$", message = "Email must end with .com or .ru")
    String email;

    @NotBlank(message = "phone number can't be blank")
    @Pattern(
            regexp = "^\\+994(50|51|55|70|77|99)[0-9]{7}$",
            message = "Phone number must start with +994 and a valid operator code"
    )
    String phoneNumber;

    @NotBlank(message = "linkedin url can't be blank")
    @Size(max = 1024)
    String linkedinUrl;

    @NotBlank(message = "instagram url can't be blank")
    @Size(max = 256)
    String instagramUrl;

    @NotBlank(message = "tiktok url can't be blank")
    @Size(max = 256)
    String tiktokUrl;
}