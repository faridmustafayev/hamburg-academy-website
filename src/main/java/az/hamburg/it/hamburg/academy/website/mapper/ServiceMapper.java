package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.ServiceEntity;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateServiceRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateServiceRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ServiceResponse;

import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.mapper.ProjectRequestMapper.PROJECT_REQUEST_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;

public enum ServiceMapper {
    SERVICE_MAPPER;

    public ServiceEntity buildServiceEntity(CreateServiceRequest request) {
        return ServiceEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(ACTIVE)
                .build();
    }

    public ServiceResponse buildServiceResponse(ServiceEntity serviceEntity) {
        return ServiceResponse.builder()
                .id(serviceEntity.getId())
                .name(serviceEntity.getName())
                .description(serviceEntity.getDescription())
                .status(serviceEntity.getStatus())
                .createdAt(serviceEntity.getCreatedAt())
                .updatedAt(serviceEntity.getUpdatedAt())
                .projectRequests(
                        serviceEntity.getProjectRequests().stream()
                                .map(PROJECT_REQUEST_MAPPER::buildProjectRequestOutput)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public void updateService(ServiceEntity serviceEntity, UpdateServiceRequest request) {
        if (request.getName() != null && !serviceEntity.getName().name().trim().isEmpty()) {
            serviceEntity.setName(request.getName());
        }

        if (request.getDescription() != null && !serviceEntity.getDescription().trim().isEmpty()) {
            serviceEntity.setDescription(request.getDescription());
        }

        serviceEntity.setStatus(IN_PROGRESS);
    }
}
