package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateReviewRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateReviewRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    void saveReview(CreateReviewRequest request);

    ReviewResponse getReview(Long id);

    List<ReviewResponse> getReviews();

    void deleteReview(Long id);

    void updateReview(Long id, UpdateReviewRequest request);
}
