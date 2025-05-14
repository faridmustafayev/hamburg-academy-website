package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.criteria.GraduateCriteria;
import az.hamburg.it.hamburg.academy.website.model.criteria.PageCriteria;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateGraduateRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateGraduateRequest;
import az.hamburg.it.hamburg.academy.website.model.response.GraduateResponse;
import az.hamburg.it.hamburg.academy.website.model.response.PageableResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.GraduateService;
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

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("v1/graduates")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GraduateController {
    GraduateService graduateService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(CREATED)
    public void saveGraduate(@ModelAttribute @Valid CreateGraduateRequest request) {
        graduateService.saveGraduate(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public GraduateResponse getGraduate(@PathVariable Long id) {
        return graduateService.getGraduate(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public PageableResponse<GraduateResponse> getGraduates(PageCriteria pageCriteria, GraduateCriteria graduateCriteria) {
        return graduateService.getGraduates(pageCriteria, graduateCriteria);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteGraduate(@PathVariable Long id) {
        graduateService.deleteGraduate(id);
    }

    @PutMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void updateGraduate(@PathVariable Long id, @ModelAttribute @Valid UpdateGraduateRequest request) {
        graduateService.updateGraduate(id, request);
    }
}