package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.ContactEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.ContactRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateContactRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateContactRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ContactResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.CONTACT_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.ContactMapper.CONTACT_MAPPER;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ContactServiceHandler implements ContactService {
    ContactRepository contactRepository;

    @Override
    public void saveContact(CreateContactRequest request) {
        var contactEntity = CONTACT_MAPPER.buildContactEntity(request);
        contactRepository.save(contactEntity);
    }

    @Override
    public ContactResponse getContact(Long id) {
        var contactEntity = fetchContactIfExist(id);
        return CONTACT_MAPPER.buildContactResponse(contactEntity);
    }

    @Override
    public List<ContactResponse> getContacts() {
        return contactRepository.findAll().stream()
                .map(CONTACT_MAPPER::buildContactResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateContact(Long id, UpdateContactRequest request) {
        var contactEntity = fetchContactIfExist(id);
        CONTACT_MAPPER.updateContact(contactEntity, request);
        contactRepository.save(contactEntity);
    }

    @Override
    public void deleteContact(Long id) {
        var contactEntity = fetchContactIfExist(id);
        contactRepository.deleteByIdCustom(contactEntity.getId());
    }

    private ContactEntity fetchContactIfExist(Long id) {
        return contactRepository.findById(id).orElseThrow(() ->
                new NotFoundException(CONTACT_NOT_FOUND.getCode(), CONTACT_NOT_FOUND.getMessage(id)));
    }

}
