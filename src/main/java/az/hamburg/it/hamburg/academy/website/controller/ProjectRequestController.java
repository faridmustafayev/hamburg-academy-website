package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateProjectRequestInput;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateProjectRequestInput;
import az.hamburg.it.hamburg.academy.website.model.response.ProjectRequestOutput;
import az.hamburg.it.hamburg.academy.website.service.abstraction.ProjectRequestService;
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
@RequestMapping("v1/project_requests")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProjectRequestController {
    ProjectRequestService projectRequestService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void saveProjectRequest(@RequestBody @Valid CreateProjectRequestInput requestInput) {
        projectRequestService.saveProjectRequest(requestInput);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public ProjectRequestOutput getProjectRequest(@PathVariable Long id) {
        return projectRequestService.getProjectRequest(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<ProjectRequestOutput> getProjectRequests() {
        return projectRequestService.getProjectRequests();
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateProjectRequest(@PathVariable Long id, @RequestBody @Valid UpdateProjectRequestInput requestInput) {
        projectRequestService.updateProjectRequest(id, requestInput);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteProjectRequest(@PathVariable Long id) {
        projectRequestService.deleteProjectRequest(id);
    }

}