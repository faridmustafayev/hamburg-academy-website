package az.hamburg.it.hamburg.academy.website.model.response;

import az.hamburg.it.hamburg.academy.website.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class CourseImageResponse {
    Long id;
    String imagePath;
    Status status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    LocalDateTime updatedAt;
}
