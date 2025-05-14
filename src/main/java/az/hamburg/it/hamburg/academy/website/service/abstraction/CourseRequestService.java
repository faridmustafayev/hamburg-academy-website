package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateCourseRequestInput;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateCourseRequestInput;
import az.hamburg.it.hamburg.academy.website.model.response.CourseRequestOutput;

import java.util.List;

public interface CourseRequestService {
    void saveCourseRequest(CreateCourseRequestInput requestInput);

    CourseRequestOutput getCourseRequest(Long id);

    List<CourseRequestOutput> getCourseRequests();

    void deleteCourseRequests(Long id);

    void updateCourseRequests(Long id, UpdateCourseRequestInput requestInput);
}
