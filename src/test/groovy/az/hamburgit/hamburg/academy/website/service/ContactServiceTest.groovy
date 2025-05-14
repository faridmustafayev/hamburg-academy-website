package az.hamburgit.hamburg.academy.website.service

import az.hamburg.it.hamburg.academy.website.dao.entity.ContactEntity
import az.hamburg.it.hamburg.academy.website.dao.repository.ContactRepository
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateContactRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateContactRequest
import az.hamburg.it.hamburg.academy.website.service.abstraction.ContactService
import az.hamburg.it.hamburg.academy.website.service.concrete.ContactServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.ContactMapper.CONTACT_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class ContactServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    ContactService contactService
    ContactRepository contactRepository

    def setup() {
        contactRepository = Mock()
        contactService = new ContactServiceHandler(contactRepository)
    }

    // saveContact
    def "TestSaveContact"() {
        given:
        def createContactRequest = random.nextObject(CreateContactRequest)
        def contactEntity = CONTACT_MAPPER.buildContactEntity(createContactRequest)

        when:
        contactService.saveContact(createContactRequest)

        then:
        1 * contactRepository.save(contactEntity)
    }

    // getContact  --> success
    def "TestGetContact success case"() {
        given:
        def id = random.nextObject(Long)
        def contact = random.nextObject(ContactEntity)

        when:
        def contactResponse = contactService.getContact(id)

        then:
        1 * contactRepository.findById(id) >> Optional.of(contact)
        contactResponse.id == contact.id
        contactResponse.address == contact.address
        contactResponse.mapUrl == contact.mapUrl
        contactResponse.email == contact.email
        contactResponse.phoneNumber == contact.phoneNumber
        contactResponse.linkedinUrl == contact.linkedinUrl
        contactResponse.instagramUrl == contact.instagramUrl
        contactResponse.tiktokUrl == contact.tiktokUrl
        contactResponse.status == contact.status
        contactResponse.createdAt == contact.createdAt
        contactResponse.updatedAt == contact.updatedAt
    }

    def "TestGetContact ContactNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        contactService.getContact(id)

        then:
        1 * contactRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "CONTACT_NOT_FOUND"
        ex.getMessage() == "No contact with id (ID: %s) was found".formatted(id)
    }

    // getContacts
    def "TestGetContacts"() {
        given:
        def contactEntity = random.nextObject(ContactEntity)
        def contacts = [contactEntity]

        when:
        def contactResponses = contactService.getContacts()
        def contactResponse = CONTACT_MAPPER.buildContactResponse(contactEntity)

        then:
        1 * contactRepository.findAll() >> contacts
        contactResponse.id == contactEntity.id
        contactResponse.address == contactEntity.address
        contactResponse.mapUrl == contactEntity.mapUrl
        contactResponse.email == contactEntity.email
        contactResponse.phoneNumber == contactEntity.phoneNumber
        contactResponse.linkedinUrl == contactEntity.linkedinUrl
        contactResponse.instagramUrl == contactEntity.instagramUrl
        contactResponse.tiktokUrl == contactEntity.tiktokUrl
        contactResponse.status == contactEntity.status
        contactResponse.createdAt == contactEntity.createdAt
        contactResponse.updatedAt == contactEntity.updatedAt
        contactResponses == [contactResponse]
    }

    // updateContact
    def "TestUpdateContact success case"() {
        given:
        def id = random.nextObject(Long)
        def updateContactRequest = random.nextObject(UpdateContactRequest)
        def contactEntity = random.nextObject(ContactEntity)

        when:
        contactService.updateContact(id, updateContactRequest)

        then:
        1 * contactRepository.findById(id) >> Optional.of(contactEntity)
        updateContactRequest.address == contactEntity.address
        updateContactRequest.mapUrl == contactEntity.mapUrl
        updateContactRequest.email == contactEntity.email
        updateContactRequest.phoneNumber == contactEntity.phoneNumber
        updateContactRequest.linkedinUrl == contactEntity.linkedinUrl
        updateContactRequest.instagramUrl == contactEntity.instagramUrl
        updateContactRequest.tiktokUrl == contactEntity.tiktokUrl
        IN_PROGRESS == contactEntity.status
        1 * contactRepository.save(contactEntity)
    }

    def "TestUpdateContact ContactNotFound case"() {
        given:
        def id = random.nextObject(Long)
        def updateContactRequest = random.nextObject(UpdateContactRequest)

        when:
        contactService.updateContact(id, updateContactRequest)

        then:
        1 * contactRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "CONTACT_NOT_FOUND"
        ex.getMessage() == "No contact with id (ID: %s) was found".formatted(id)
        0 * contactRepository.save(_)
    }

    // deleteContact
    def "TestDeleteContact success case"() {
        given:
        def id = random.nextObject(Long)
        def contactEntity = random.nextObject(ContactEntity)

        when:
        contactService.deleteContact(id)

        then:
        1 * contactRepository.findById(id) >> Optional.of(contactEntity)
        1 * contactRepository.deleteByIdCustom(contactEntity.id)
    }

    def "TestDeleteContact ContactNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        contactService.deleteContact(id)

        then:
        1 * contactRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "CONTACT_NOT_FOUND"
        ex.getMessage() == "No contact with id (ID: %s) was found".formatted(id)
        0 * contactRepository.deleteByIdCustom(id)
    }

}
