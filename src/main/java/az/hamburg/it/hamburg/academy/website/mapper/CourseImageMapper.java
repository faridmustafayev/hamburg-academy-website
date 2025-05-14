package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.CourseImageEntity;
import az.hamburg.it.hamburg.academy.website.model.request.CreateOrUpdateCourseImageRequest;
import az.hamburg.it.hamburg.academy.website.model.response.CourseImageResponse;

import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE;

public enum CourseImageMapper {
    COURSE_IMAGE_MAPPER;

    public CourseImageEntity buildCourseImageEntity(CreateOrUpdateCourseImageRequest request) {
        return CourseImageEntity.builder()
                .status(ACTIVE)
                .build();
    }

    public CourseImageResponse buildCourseImageResponse(CourseImageEntity entity) {
        return CourseImageResponse.builder()
                .id(entity.getId())
                .imagePath(entity.getImagePath())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}