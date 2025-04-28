package az.hamburg.it.hamburg.academy.website.model.request.create;

import az.hamburg.it.hamburg.academy.website.annotation.ValidImage;
import az.hamburg.it.hamburg.academy.website.model.enums.SpecializationType;
import az.hamburg.it.hamburg.academy.website.serialization.CustomFileSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreateGraduateRequest {
    @NotBlank(message = "Full name can not be blank")
    @Size(max = 128, message = "Full name can't exceed 128 characters")
    String fullName;

    @NotNull(message = "Image file must not be null")
    @ValidImage
    @JsonSerialize(using = CustomFileSerializer.class)
    MultipartFile imageFile;

    @NotEmpty(message = "At least one specialization must be selected")
    @Size(max = 2, message = "You can select up to 2 specializations")
    List<@NotNull(message = "Specialization id must not be null") Long> specializationIds;
}