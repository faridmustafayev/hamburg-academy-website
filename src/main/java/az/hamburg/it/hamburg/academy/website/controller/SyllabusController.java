package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateSyllabusRequest;
import az.hamburg.it.hamburg.academy.website.model.response.SyllabusResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.SyllabusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/syllabuses")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SyllabusController {
    SyllabusService syllabusService;

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public SyllabusResponse getSyllabus(@PathVariable Long id) {
        return syllabusService.getSyllabus(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<SyllabusResponse> getSyllabuses() {
        return syllabusService.getSyllabuses();
    }

    @PutMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void updateSyllabus(@PathVariable Long id, @ModelAttribute @Valid UpdateSyllabusRequest syllabusRequest) {
        syllabusService.updateSyllabus(id, syllabusRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteSyllabus(@PathVariable Long id) {
        syllabusService.deleteSyllabus(id);
    }
}
