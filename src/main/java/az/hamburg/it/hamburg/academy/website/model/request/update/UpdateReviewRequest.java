package az.hamburg.it.hamburg.academy.website.model.request.update;

import az.hamburg.it.hamburg.academy.website.annotation.ValidImage;
import az.hamburg.it.hamburg.academy.website.annotation.ValidRating;
import az.hamburg.it.hamburg.academy.website.serialization.CustomFileSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
public class UpdateReviewRequest {
    @Size(max = 64, message = "student name can't exceed 64 characters")
    String studentName;

    @Size(max = 128, message = "student surname can't exceed 128 characters")
    String studentSurname;

    String comment;

    @ValidImage
    @JsonSerialize(using = CustomFileSerializer.class)
    MultipartFile imageFile;

    @ValidRating
    Double rating;

    Long specializationId;

}