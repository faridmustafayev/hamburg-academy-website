package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.ContactEntity
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateContactRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateContactRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.ContactMapper.CONTACT_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class ContactMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // buildContactEntity
    def "TestBuildContactEntity"() {
        given:
        def contactRequest = random.nextObject(CreateContactRequest)

        when:
        def contact = CONTACT_MAPPER.buildContactEntity(contactRequest)

        then:
        contactRequest.address == contact.address
        contactRequest.mapUrl == contact.mapUrl
        contactRequest.email == contact.email
        contactRequest.phoneNumber == contact.phoneNumber
        contactRequest.linkedinUrl == contact.linkedinUrl
        contactRequest.instagramUrl == contact.instagramUrl
        contactRequest.tiktokUrl == contact.tiktokUrl
        ACTIVE == contact.status
    }

    // buildContactResponse
    def "TestBuildContactResponse"() {
        given:
        def contactEntity = random.nextObject(ContactEntity)

        when:
        def contact = CONTACT_MAPPER.buildContactResponse(contactEntity)

        then:
        contactEntity.id == contact.id
        contactEntity.address == contact.address
        contactEntity.mapUrl == contact.mapUrl
        contactEntity.email == contact.email
        contactEntity.phoneNumber == contact.phoneNumber
        contactEntity.linkedinUrl == contact.linkedinUrl
        contactEntity.instagramUrl == contact.instagramUrl
        contactEntity.tiktokUrl == contact.tiktokUrl
        contactEntity.status == contact.status
        contactEntity.createdAt == contact.createdAt
        contactEntity.updatedAt == contact.updatedAt
    }

    // updateContact
    def "TestUpdateContact"() {
        given:
        def contact = random.nextObject(ContactEntity)
        def request = random.nextObject(UpdateContactRequest)

        when:
        CONTACT_MAPPER.updateContact(contact, request)

        then:
        contact.address == request.address
        contact.mapUrl == request.mapUrl
        contact.email == request.email
        contact.phoneNumber == request.phoneNumber
        contact.linkedinUrl == request.linkedinUrl
        contact.instagramUrl == request.instagramUrl
        contact.tiktokUrl == request.tiktokUrl
        contact.status == IN_PROGRESS
    }
}
