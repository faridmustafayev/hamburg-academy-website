package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateSyllabusRequest;
import az.hamburg.it.hamburg.academy.website.model.response.SyllabusResponse;

import java.util.List;

public interface SyllabusService {
    SyllabusResponse getSyllabus(Long id);

    List<SyllabusResponse> getSyllabuses();

    void updateSyllabus(Long id, UpdateSyllabusRequest syllabusRequest);

    void deleteSyllabus(Long id);
}
