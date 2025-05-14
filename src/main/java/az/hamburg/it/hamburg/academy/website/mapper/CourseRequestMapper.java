package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.CourseRequestEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateCourseRequestInput;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateCourseRequestInput;
import az.hamburg.it.hamburg.academy.website.model.response.CourseRequestOutput;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;

public enum CourseRequestMapper {
    COURSE_REQUEST_MAPPER;

    public CourseRequestEntity buildCourseRequestEntity(CreateCourseRequestInput requestInput,
                                                        List<SpecializationEntity> specializations) {
        return CourseRequestEntity.builder()
                .fullName(requestInput.getFullName())
                .phoneNumber(requestInput.getPhoneNumber())
                .specializations(specializations)
                .status(ACTIVE)
                .build();
    }

    public CourseRequestOutput buildCourseRequestOutput(CourseRequestEntity request) {
        return CourseRequestOutput.builder()
                .id(request.getId())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .specializationIds(
                        request.getSpecializations().stream()
                                .map(SpecializationEntity::getId)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public void updateCourseRequest(CourseRequestEntity entity, UpdateCourseRequestInput requestInput) {
        if (requestInput.getFullName() != null && !requestInput.getFullName().trim().isEmpty()) {
            entity.setFullName(requestInput.getFullName());
        }

        if (requestInput.getPhoneNumber() != null && !requestInput.getPhoneNumber().trim().isEmpty()) {
            entity.setPhoneNumber(requestInput.getPhoneNumber());
        }

        entity.setStatus(IN_PROGRESS);
    }
}
