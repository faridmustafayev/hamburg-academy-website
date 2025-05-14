package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateSpecializationRequest;
import az.hamburg.it.hamburg.academy.website.model.response.SpecializationResponse;

import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.mapper.CourseRequestMapper.COURSE_REQUEST_MAPPER;
import static az.hamburg.it.hamburg.academy.website.mapper.GraduateMapper.GRADUATE_MAPPER;
import static az.hamburg.it.hamburg.academy.website.mapper.InstructorMapper.INSTRUCTOR_MAPPER;
import static az.hamburg.it.hamburg.academy.website.mapper.ReviewMapper.REVIEW_MAPPER;
import static az.hamburg.it.hamburg.academy.website.mapper.SyllabusMapper.SYLLABUS_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;

public enum SpecializationMapper {
    SPECIALIZATION_MAPPER;

    public SpecializationResponse buildSpecializationResponse(SpecializationEntity specialization) {
        return SpecializationResponse.builder()
                .id(specialization.getId())
                .name(specialization.getName())
                .description(specialization.getDescription())
                .status(specialization.getStatus())
                .createdAt(specialization.getCreatedAt())
                .updatedAt(specialization.getUpdatedAt())
                .instructors(
                        specialization.getInstructors()
                                .stream()
                                .map(INSTRUCTOR_MAPPER::buildInstructorResponse)
                                .collect(Collectors.toList()))
                .reviews(
                        specialization.getReviews().stream()
                                .map(REVIEW_MAPPER::buildReviewResponse)
                                .collect(Collectors.toList())
                )
                .graduates(
                        specialization.getGraduates().stream()
                                .map(GRADUATE_MAPPER::buildGraduateResponse)
                                .collect(Collectors.toList())
                )
                .courseRequests(
                        specialization.getCourseRequests().stream()
                                .map(COURSE_REQUEST_MAPPER::buildCourseRequestOutput)
                                .collect(Collectors.toList())
                )
                .syllabusResponse(
                        specialization.getSyllabus() != null
                                ? SYLLABUS_MAPPER.buildSyllabusResponse(specialization.getSyllabus())
                                : null
                )
                .build();
    }

    public void updateSpecialization(SpecializationEntity specialization, UpdateSpecializationRequest request) {
        if (specialization.getDescription() != null && !specialization.getDescription().trim().isEmpty()) {
            specialization.setDescription(request.getDescription());
        }

        specialization.setStatus(IN_PROGRESS);
    }
}
