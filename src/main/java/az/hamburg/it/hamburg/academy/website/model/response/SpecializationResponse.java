package az.hamburg.it.hamburg.academy.website.model.response;

import az.hamburg.it.hamburg.academy.website.model.enums.SpecializationType;
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
public class SpecializationResponse {
    Long id;
    SpecializationType name;
    String description;
    Status status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    @Builder.Default
    List<InstructorResponse> instructors = new ArrayList<>();
    @Builder.Default
    List<ReviewResponse> reviews = new ArrayList<>();
    @Builder.Default
    List<GraduateResponse> graduates = new ArrayList<>();
    @Builder.Default
    List<CourseRequestOutput> courseRequests = new ArrayList<>();
    SyllabusResponse syllabusResponse;
}
