package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.InstructorEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateInstructorRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateInstructorRequest;
import az.hamburg.it.hamburg.academy.website.model.response.InstructorResponse;

import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;

public enum InstructorMapper {
    INSTRUCTOR_MAPPER;

    public InstructorEntity buildInstructorEntity(CreateInstructorRequest request, SpecializationEntity specialization) {
        return InstructorEntity.builder()
                .fullName(request.getFullName())
                .info(request.getInfo())
                .linkedin(request.getLinkedin())
                .imagePath(request.getImageFile() != null ? request.getImageFile().getOriginalFilename() : null)
                .specialization(specialization)
                .status(ACTIVE)
                .build();
    }

    public InstructorResponse buildInstructorResponse(InstructorEntity instructor) {
        return InstructorResponse.builder()
                .id(instructor.getId())
                .fullName(instructor.getFullName())
                .info(instructor.getInfo())
                .linkedin(instructor.getLinkedin())
                .imagePath(instructor.getImagePath())
                .status(instructor.getStatus())
                .createdAt(instructor.getCreatedAt())
                .updatedAt(instructor.getUpdatedAt())
                .specializationId(instructor.getSpecialization().getId())
                .build();
    }

    public void updateInstructor(InstructorEntity instructor, UpdateInstructorRequest instructorRequest) {
        if (instructorRequest.getFullName() != null && !instructorRequest.getFullName().trim().isEmpty()) {
            instructor.setFullName(instructorRequest.getFullName());
        }

        if (instructorRequest.getInfo() != null && !instructorRequest.getInfo().trim().isEmpty()) {
            instructor.setInfo(instructorRequest.getInfo());
        }

        if (instructorRequest.getLinkedin() != null && !instructorRequest.getLinkedin().trim().isEmpty()) {
            instructor.setLinkedin(instructorRequest.getLinkedin());
        }

        if (instructorRequest.getSpecializationId() != null) {
            instructor.getSpecialization().setId(instructorRequest.getSpecializationId());
        }

        if (instructorRequest.getImageFile() != null) {
            instructor.setImagePath(instructorRequest.getImageFile().getOriginalFilename());
        }

        instructor.setStatus(IN_PROGRESS);
    }

}