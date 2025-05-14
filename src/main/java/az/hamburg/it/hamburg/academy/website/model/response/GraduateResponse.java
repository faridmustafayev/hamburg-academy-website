package az.hamburg.it.hamburg.academy.website.model.response;

import az.hamburg.it.hamburg.academy.website.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class GraduateResponse {
    Long id;
    String fullName;
    String imagePath;
    Status status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    @Builder.Default
    List<DiplomaResponse> diplomas = new ArrayList<>();
    List<Long> specializationIds;
}