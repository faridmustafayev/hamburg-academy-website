package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.criteria.GraduateCriteria;
import az.hamburg.it.hamburg.academy.website.model.criteria.PageCriteria;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateGraduateRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateGraduateRequest;
import az.hamburg.it.hamburg.academy.website.model.response.GraduateResponse;
import az.hamburg.it.hamburg.academy.website.model.response.PageableResponse;

public interface GraduateService {
    void saveGraduate(CreateGraduateRequest request);

    GraduateResponse getGraduate(Long id);

    PageableResponse<GraduateResponse> getGraduates(PageCriteria pageCriteria, GraduateCriteria graduateCriteria);

    void deleteGraduate(Long id);

    void updateGraduate(Long id, UpdateGraduateRequest request);
}
