package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateSpecializationRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateSpecializationRequest;
import az.hamburg.it.hamburg.academy.website.model.response.SpecializationResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.SpecializationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("v1/specializations")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SpecializationController {
    SpecializationService specializationService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(CREATED)
    public void saveSpecialization(@ModelAttribute @Valid CreateSpecializationRequest request) {
        specializationService.saveSpecialization(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public SpecializationResponse getSpecialization(@PathVariable Long id) {
        return specializationService.getSpecialization(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<SpecializationResponse> getAll() {
        return specializationService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteSpecialization(@PathVariable Long id) {
        specializationService.deleteSpecialization(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateSpecialization(@PathVariable Long id, @RequestBody @Valid UpdateSpecializationRequest request) {
        specializationService.updateSpecialization(id, request);
    }
}
