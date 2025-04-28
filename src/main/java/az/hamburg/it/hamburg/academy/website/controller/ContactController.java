package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateContactRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateContactRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ContactResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.ContactService;
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
@RequiredArgsConstructor
@RequestMapping("v1/contacts")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ContactController {
    ContactService contactService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void saveContact(@RequestBody @Valid CreateContactRequest request) {
        contactService.saveContact(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public ContactResponse getContact(@PathVariable Long id) {
        return contactService.getContact(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<ContactResponse> getContacts() {
        return contactService.getContacts();
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateContact(@PathVariable Long id, @RequestBody @Valid UpdateContactRequest request) {
        contactService.updateContact(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
    }
}
