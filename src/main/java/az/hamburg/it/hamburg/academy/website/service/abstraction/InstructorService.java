package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateInstructorRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateInstructorRequest;
import az.hamburg.it.hamburg.academy.website.model.response.InstructorResponse;

import java.util.List;

public interface InstructorService {
    void saveInstructor(CreateInstructorRequest request);

    InstructorResponse getInstructor(Long id);

    List<InstructorResponse> getAll();

    void deleteInstructor(Long id);

    void updateInstructor(Long id, UpdateInstructorRequest instructorRequest);
}
