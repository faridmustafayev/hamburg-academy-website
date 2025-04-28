package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateInstructorRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateInstructorRequest;
import az.hamburg.it.hamburg.academy.website.model.response.InstructorResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.InstructorService;
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
@RequestMapping("/v1/instructors")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class InstructorController {
    InstructorService instructorService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(CREATED)
    public void saveInstructor(@ModelAttribute @Valid CreateInstructorRequest request) {
        instructorService.saveInstructor(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public InstructorResponse getInstructor(@PathVariable Long id) {
        return instructorService.getInstructor(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<InstructorResponse> getAll() {
        return instructorService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
    }

    @PutMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void updateInstructor(@PathVariable Long id, @ModelAttribute @Valid UpdateInstructorRequest instructorRequest) {
        instructorService.updateInstructor(id, instructorRequest);
    }
}