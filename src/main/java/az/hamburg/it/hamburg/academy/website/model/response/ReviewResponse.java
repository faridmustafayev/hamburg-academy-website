package az.hamburg.it.hamburg.academy.website.model.response;

import az.hamburg.it.hamburg.academy.website.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReviewResponse {
    Long id;
    String studentName;
    String studentSurname;
    String comment;
    String imagePath;
    Double rating;
    Status status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Long specializationId;
}
