package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.ReviewEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateReviewRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateReviewRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ReviewResponse;

import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;

public enum ReviewMapper {
    REVIEW_MAPPER;

    public ReviewEntity buildReviewEntity(CreateReviewRequest request, SpecializationEntity specialization) {
        return ReviewEntity.builder()
                .studentName(request.getStudentName())
                .studentSurname(request.getStudentSurname())
                .comment(request.getComment())
                .rating(request.getRating())
                .imagePath(request.getImageFile() != null ? request.getImageFile().getOriginalFilename() : null)
                .specialization(specialization)
                .status(ACTIVE)
                .build();
    }

    public ReviewResponse buildReviewResponse(ReviewEntity review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .studentName(review.getStudentName())
                .studentSurname(review.getStudentSurname())
                .comment(review.getComment())
                .imagePath(review.getImagePath())
                .rating(review.getRating())
                .status(review.getStatus())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .specializationId(review.getSpecialization() != null ? review.getSpecialization().getId() : null)
                .build();
    }

    public void updateReview(ReviewEntity review, UpdateReviewRequest request) {
        if (request.getStudentName() != null && !request.getStudentName().trim().isEmpty()) {
            review.setStudentName(request.getStudentName());
        }

        if (request.getStudentSurname() != null && !request.getStudentSurname().trim().isEmpty()) {
            review.setStudentSurname(request.getStudentSurname());
        }

        if (request.getComment() != null && !request.getComment().trim().isEmpty()) {
            review.setComment(request.getComment());
        }

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }

        if (request.getImageFile() != null) {
            review.setImagePath(request.getImageFile().getOriginalFilename());
        }

        if (request.getSpecializationId() != null) {
            review.getSpecialization().setId(request.getSpecializationId());
        }

        review.setStatus(IN_PROGRESS);
    }
}
