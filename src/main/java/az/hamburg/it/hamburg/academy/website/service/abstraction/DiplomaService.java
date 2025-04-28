package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateDiplomaRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateDiplomaRequest;
import az.hamburg.it.hamburg.academy.website.model.response.DiplomaResponse;

import java.util.List;

public interface DiplomaService {
    void saveDiploma(CreateDiplomaRequest request);

    void deleteDiploma(Long id);

    void updateDiploma(Long id, UpdateDiplomaRequest request);

    DiplomaResponse getDiploma(Long id);

    List<DiplomaResponse> getDiplomas();
}
