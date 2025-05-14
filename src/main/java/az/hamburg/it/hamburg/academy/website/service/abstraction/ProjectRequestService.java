package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateProjectRequestInput;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateProjectRequestInput;
import az.hamburg.it.hamburg.academy.website.model.response.ProjectRequestOutput;

import java.util.List;

public interface ProjectRequestService {
    void saveProjectRequest(CreateProjectRequestInput requestInput);

    ProjectRequestOutput getProjectRequest(Long id);

    List<ProjectRequestOutput> getProjectRequests();

    void updateProjectRequest(Long id, UpdateProjectRequestInput requestInput);

    void deleteProjectRequest(Long id);
}
