package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateServiceRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateServiceRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ServiceResponse;

import java.util.List;

public interface ServiceAbstraction {
    void saveService(CreateServiceRequest request);

    ServiceResponse getService(Long id);

    List<ServiceResponse> getServices();

    void updateService(Long id, UpdateServiceRequest request);

    void deleteService(Long id);
}
