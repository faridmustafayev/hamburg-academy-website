package az.hamburg.it.hamburg.academy.website.model.request.create;

import az.hamburg.it.hamburg.academy.website.annotation.ValidImage;
import az.hamburg.it.hamburg.academy.website.serialization.CustomFileSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreateInstructorRequest {
    @NotBlank(message = "Full name can not be blank")
    @Size(max = 128, message = "Full name can't exceed 128 characters")
    String fullName;

    @NotBlank(message = "Info can't be blank")
    String info;

    @NotBlank(message = "linkedin url can't be blank")
    @Size(max = 1024)
    String linkedin;

    @NotNull(message = "Image file must not be null")
    @ValidImage
    @JsonSerialize(using = CustomFileSerializer.class)
    MultipartFile imageFile;

    @NotNull(message = "Specialization ID must not be null")
    Long specializationId;
}