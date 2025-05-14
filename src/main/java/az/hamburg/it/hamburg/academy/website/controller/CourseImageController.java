package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.CreateOrUpdateCourseImageRequest;
import az.hamburg.it.hamburg.academy.website.model.response.CourseImageResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.CourseImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping("v1/course_images")
public class CourseImageController {
    CourseImageService courseImageService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(CREATED)
    public void saveCourseImage(@ModelAttribute @Valid CreateOrUpdateCourseImageRequest request) {
        courseImageService.saveCourseImage(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public CourseImageResponse getCourseImage(@PathVariable Long id) {
        return courseImageService.getCourseImage(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<CourseImageResponse> getCourseImages() {
        return courseImageService.getCourseImages();
    }

    @PatchMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void updateCourseImage(@PathVariable Long id, @ModelAttribute @Valid CreateOrUpdateCourseImageRequest request) {
        courseImageService.updateCourseImage(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCourseImage(@PathVariable Long id) {
        courseImageService.deleteCourseImage(id);
    }
}
