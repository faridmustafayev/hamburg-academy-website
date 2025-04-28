package az.hamburg.it.hamburg.academy.website.model.request;

import az.hamburg.it.hamburg.academy.website.annotation.ValidImage;
import az.hamburg.it.hamburg.academy.website.serialization.CustomFileSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class CreateOrUpdateCourseImageRequest {
    @NotNull(message = "Image file must not be null")
    @ValidImage
    @JsonSerialize(using = CustomFileSerializer.class)
    MultipartFile imageFile;
}
