package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateDiplomaRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateDiplomaRequest;
import az.hamburg.it.hamburg.academy.website.model.response.DiplomaResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.DiplomaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("v1/diplomas")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DiplomaController {
    DiplomaService diplomaService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(CREATED)
    public void saveDiploma(@ModelAttribute @Valid CreateDiplomaRequest request) {
        diplomaService.saveDiploma(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteDiploma(@PathVariable Long id) {
        diplomaService.deleteDiploma(id);
    }

    @PutMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void updateDiploma(@PathVariable Long id, @ModelAttribute @Valid UpdateDiplomaRequest request) {
        diplomaService.updateDiploma(id, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public DiplomaResponse getDiploma(@PathVariable Long id) {
        return diplomaService.getDiploma(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<DiplomaResponse> getDiplomas() {
        return diplomaService.getDiplomas();
    }
}
