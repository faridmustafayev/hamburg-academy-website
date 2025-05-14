package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateSpecializationRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateSpecializationRequest;
import az.hamburg.it.hamburg.academy.website.model.response.SpecializationResponse;

import java.util.List;

public interface SpecializationService {
    void saveSpecialization(CreateSpecializationRequest request);

    SpecializationResponse getSpecialization(Long id);

    List<SpecializationResponse> getAll();

    void deleteSpecialization(Long id);

    void updateSpecialization(Long id, UpdateSpecializationRequest request);

    SpecializationEntity fetchSpecializationIfExist(Long id);
}
