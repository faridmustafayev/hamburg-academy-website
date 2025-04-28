package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateReviewRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateReviewRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ReviewResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("v1/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(CREATED)
    public void saveReview(@ModelAttribute @Valid CreateReviewRequest request) {
        reviewService.saveReview(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public ReviewResponse getReview(@PathVariable Long id) {
        return reviewService.getReview(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<ReviewResponse> getReviews() {
        return reviewService.getReviews();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @PutMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void updateReview(@PathVariable Long id, @ModelAttribute @Valid UpdateReviewRequest request) {
        reviewService.updateReview(id, request);
    }
}
