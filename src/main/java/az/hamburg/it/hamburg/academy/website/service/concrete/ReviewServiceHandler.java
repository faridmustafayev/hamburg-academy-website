package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.ReviewEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.ReviewRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateReviewRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateReviewRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ReviewResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService;
import az.hamburg.it.hamburg.academy.website.service.abstraction.ReviewService;
import az.hamburg.it.hamburg.academy.website.service.abstraction.SpecializationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.REVIEW_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.ReviewMapper.REVIEW_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.DELETED;
import static az.hamburg.it.hamburg.academy.website.util.FileUtils.extractFileNameFromUrl;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReviewServiceHandler implements ReviewService {
    final ReviewRepository reviewRepository;
    final SpecializationService specializationService;
    final MinioService minioService;
    @Value("${minio.buckets.reviews.name}")
    String reviewBucket;

    @Override
    public void saveReview(CreateReviewRequest request) {
        var specializationEntity = specializationService.fetchSpecializationIfExist(request.getSpecializationId());
        var reviewEntity = REVIEW_MAPPER.buildReviewEntity(request, specializationEntity);

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            var imageFile = minioService.uploadFile(request.getImageFile(), reviewBucket);
            var fileUrl = minioService.getFileUrl(imageFile, reviewBucket);
            reviewEntity.setImagePath(fileUrl);
        }

        reviewRepository.save(reviewEntity);
    }

    @Override
    public ReviewResponse getReview(Long id) {
        var reviewEntity = fetchReviewIfExist(id);
        return REVIEW_MAPPER.buildReviewResponse(reviewEntity);
    }

    @Override
    public List<ReviewResponse> getReviews() {
        return reviewRepository.findAll().stream()
                .map(REVIEW_MAPPER::buildReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReview(Long id) {
        var reviewEntity = fetchReviewIfExist(id);
        reviewEntity.setStatus(DELETED);
        reviewRepository.save(reviewEntity);
    }

    @Override
    public void updateReview(Long id, UpdateReviewRequest request) {
        var reviewEntity = fetchReviewIfExist(id);

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            if (reviewEntity.getImagePath() != null) {
                var fileName = extractFileNameFromUrl(reviewEntity.getImagePath());
                minioService.deleteFile(fileName, reviewBucket);
            }
            var uploadFile = minioService.uploadFile(request.getImageFile(), reviewBucket);
            var fileUrl = minioService.getFileUrl(uploadFile, reviewBucket);
            reviewEntity.setImagePath(fileUrl);
        }

        REVIEW_MAPPER.updateReview(reviewEntity, request);
        reviewRepository.save(reviewEntity);
    }

    private ReviewEntity fetchReviewIfExist(Long id) {
        return reviewRepository.findById(id).orElseThrow(() ->
                new NotFoundException(REVIEW_NOT_FOUND.getCode(), REVIEW_NOT_FOUND.getMessage(id)));
    }

}
