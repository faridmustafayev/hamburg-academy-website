package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.request.CreateOrUpdateCourseImageRequest;
import az.hamburg.it.hamburg.academy.website.model.response.CourseImageResponse;

import java.util.List;

public interface CourseImageService {
    void saveCourseImage(CreateOrUpdateCourseImageRequest request);

    CourseImageResponse getCourseImage(Long id);

    List<CourseImageResponse> getCourseImages();

    void updateCourseImage(Long id, CreateOrUpdateCourseImageRequest request);

    void deleteCourseImage(Long id);
}
