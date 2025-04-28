package az.hamburg.it.hamburg.academy.website.model.request.create;

import az.hamburg.it.hamburg.academy.website.annotation.ValidImage;
import az.hamburg.it.hamburg.academy.website.annotation.ValidRating;
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
public class CreateReviewRequest {
    @NotBlank(message = "student name can not be blank")
    @Size(max = 64, message = "student name can't exceed 64 characters")
    String studentName;

    @NotBlank(message = "student surname can not be blank")
    @Size(max = 128, message = "student surname can't exceed 128 characters")
    String studentSurname;

    @NotBlank(message = "comment can't be blank")
    String comment;

    @NotNull(message = "Image file must not be null")
    @ValidImage
    @JsonSerialize(using = CustomFileSerializer.class)
    MultipartFile imageFile;

    @ValidRating
    Double rating;

    @NotNull(message = "Specialization ID must not be null")
    Long specializationId;
}