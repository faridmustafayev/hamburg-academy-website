package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateCourseRequestInput;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateCourseRequestInput;
import az.hamburg.it.hamburg.academy.website.model.response.CourseRequestOutput;
import az.hamburg.it.hamburg.academy.website.service.abstraction.CourseRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("v1/course_requests")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CourseRequestController {
    CourseRequestService courseRequestService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void saveCourseRequest(@RequestBody @Valid CreateCourseRequestInput requestInput) {
        courseRequestService.saveCourseRequest(requestInput);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public CourseRequestOutput getCourseRequest(@PathVariable Long id) {
        return courseRequestService.getCourseRequest(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<CourseRequestOutput> getCourseRequests() {
        return courseRequestService.getCourseRequests();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCourseRequests(@PathVariable Long id) {
        courseRequestService.deleteCourseRequests(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateCourseRequests(@PathVariable Long id, @RequestBody @Valid UpdateCourseRequestInput requestInput) {
        courseRequestService.updateCourseRequests(id, requestInput);
    }
}
