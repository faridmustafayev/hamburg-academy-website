package az.hamburg.it.hamburg.academy.website.model.request.create;

import az.hamburg.it.hamburg.academy.website.annotation.ValidImage;
import az.hamburg.it.hamburg.academy.website.annotation.ValidPdf;
import az.hamburg.it.hamburg.academy.website.model.enums.SpecializationType;
import az.hamburg.it.hamburg.academy.website.serialization.CustomFileSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateSpecializationRequest {
    @NotNull(message = "Specialization type can't be null")
    SpecializationType name;

    @NotBlank(message = "description can't be blank")
    String description;

    @NotNull(message = "Image file must not be null")
    @ValidImage
    @JsonSerialize(using = CustomFileSerializer.class)
    MultipartFile imageFile;

    @NotNull(message = "PDF file must not be null")
    @ValidPdf
    @JsonSerialize(using = CustomFileSerializer.class)
    MultipartFile pdfFile;
}