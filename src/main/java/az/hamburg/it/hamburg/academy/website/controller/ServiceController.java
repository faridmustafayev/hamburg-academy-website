package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateServiceRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateServiceRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ServiceResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.ServiceAbstraction;
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
@RequestMapping("v1/services")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ServiceController {
    ServiceAbstraction serviceAbstraction;

    @PostMapping
    @ResponseStatus(CREATED)
    public void saveService(@RequestBody @Valid CreateServiceRequest request) {
        serviceAbstraction.saveService(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public ServiceResponse getService(@PathVariable Long id) {
        return serviceAbstraction.getService(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<ServiceResponse> getServices() {
        return serviceAbstraction.getServices();
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateService(@PathVariable Long id, @RequestBody @Valid UpdateServiceRequest request) {
        serviceAbstraction.updateService(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteService(@PathVariable Long id) {
        serviceAbstraction.deleteService(id);
    }
}
