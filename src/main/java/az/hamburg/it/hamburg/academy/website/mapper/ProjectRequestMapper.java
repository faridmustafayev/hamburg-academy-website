package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.ProjectRequestEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.ServiceEntity;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateProjectRequestInput;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateProjectRequestInput;
import az.hamburg.it.hamburg.academy.website.model.response.ProjectRequestOutput;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;

public enum ProjectRequestMapper {
    PROJECT_REQUEST_MAPPER;

    public ProjectRequestEntity buildProjectRequestEntity(CreateProjectRequestInput requestInput, List<ServiceEntity> services) {
        return ProjectRequestEntity.builder()
                .fullName(requestInput.getFullName())
                .email(requestInput.getEmail())
                .phoneNumber(requestInput.getPhoneNumber())
                .services(services)
                .status(ACTIVE)
                .build();
    }

    public ProjectRequestOutput buildProjectRequestOutput(ProjectRequestEntity request) {
        return ProjectRequestOutput.builder()
                .id(request.getId())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .serviceIds(
                        request.getServices().stream()
                                .map(ServiceEntity::getId)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public void updateProjectRequest(ProjectRequestEntity entity, UpdateProjectRequestInput requestInput) {
        if (requestInput.getFullName() != null && !requestInput.getFullName().trim().isEmpty()) {
            entity.setFullName(requestInput.getFullName());
        }

        if (requestInput.getEmail() != null && !requestInput.getEmail().trim().isEmpty()) {
            entity.setEmail(requestInput.getEmail());
        }

        if (requestInput.getPhoneNumber() != null && !requestInput.getPhoneNumber().trim().isEmpty()) {
            entity.setPhoneNumber(requestInput.getPhoneNumber());
        }

        entity.setStatus(IN_PROGRESS);
    }
}
