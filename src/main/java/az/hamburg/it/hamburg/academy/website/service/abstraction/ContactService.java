package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.request.create.CreateContactRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateContactRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ContactResponse;

import java.util.List;

public interface ContactService {
    void saveContact(CreateContactRequest request);

    ContactResponse getContact(Long id);

    List<ContactResponse> getContacts();

    void updateContact(Long id, UpdateContactRequest request);

    void deleteContact(Long id);
}
