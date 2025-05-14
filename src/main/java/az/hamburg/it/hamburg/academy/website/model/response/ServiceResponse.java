package az.hamburg.it.hamburg.academy.website.model.response;

import az.hamburg.it.hamburg.academy.website.model.enums.ServiceType;
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
@Builder
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ServiceResponse {
    Long id;
    ServiceType name;
    String description;
    Status status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    @Builder.Default
    List<ProjectRequestOutput> projectRequests = new ArrayList<>();
}
