package az.hamburgit.hamburg.academy.website.controller

import az.hamburg.it.hamburg.academy.website.controller.ContactController
import az.hamburg.it.hamburg.academy.website.exception.ErrorHandler
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateContactRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateContactRequest
import az.hamburg.it.hamburg.academy.website.model.response.ContactResponse
import az.hamburg.it.hamburg.academy.website.service.abstraction.ContactService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime

import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import static org.springframework.http.HttpHeaders.AUTHORIZATION
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

class ContactControllerTest extends Specification {
    ContactService contactService
    ContactController contactController
    MockMvc mockMvc

    def setup() {
        contactService = Mock()
        contactController = new ContactController(contactService)
        mockMvc = MockMvcBuilders.standaloneSetup(contactController)
                .setControllerAdvice(ErrorHandler.class)
                .build()
    }

    // createContact
    def "TestCreateContact success case"() {
        given:
        def url = "/v1/contacts"
        def contactRequest = CreateContactRequest.builder()
                .address("address")
                .mapUrl("mapUrl")
                .email("hamburgAcademy@gmail.com")
                .phoneNumber("+994503044665")
                .linkedinUrl("linkedinUrl")
                .instagramUrl("instagramUrl")
                .tiktokUrl("tiktokUrl")
                .build()
        def jsonContactRequest =
                """
                   {
                       "address": "address",
                       "mapUrl": "mapUrl",
                       "email": "hamburgAcademy@gmail.com",
                       "phoneNumber": "+994503044665",
                       "linkedinUrl": "linkedinUrl",
                       "instagramUrl": "instagramUrl",
                       "tiktokUrl": "tiktokUrl"
                   }
                """

        when:
        def jsonResponse = mockMvc.perform(
                post(url)
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "")
                        .content(jsonContactRequest)
        ).andReturn()

        then:
        1 * contactService.saveContact(contactRequest)
        jsonResponse.response.status == CREATED.value()
    }

    // getContactById
    def "TestGetContact success case"() {
        given:
        def id = 1L
        def url = "/v1/contacts/$id"
        def contactResponse = ContactResponse.builder()
                .id(id)
                .address("address")
                .mapUrl("mapUrl")
                .email("hamburgAcademy@gmail.com")
                .phoneNumber("+994503044665")
                .linkedinUrl("linkedinUrl")
                .instagramUrl("instagramUrl")
                .tiktokUrl("tiktokUrl")
                .linkedinUrl("linkedinUrl")
                .status(ACTIVE)
                .createdAt(LocalDateTime.of(2025, 7, 8, 3, 2, 1, 4))
                .updatedAt(LocalDateTime.of(2025, 8, 8, 3, 2, 1, 4))
                .build()

        def objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS)

        def expectedJson = objectMapper.writeValueAsString(contactResponse)

        when:
        def jsonResponse = mockMvc.perform(
                get(url)
                        .contentType(APPLICATION_JSON)
        ).andReturn()

        then:
        1 * contactService.getContact(id) >> contactResponse
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(expectedJson.toString(), jsonResponse.response.contentAsString.toString(), true)
    }

    // getContacts
    def "TestGetContacts success case"() {
        given:
        def url = "/v1/contacts"

        def contact1 = ContactResponse.builder()
                .id(1L)
                .address("address 1")
                .mapUrl("mapUrl 1")
                .email("email1@gmail.com")
                .phoneNumber("+994501111111")
                .linkedinUrl("linkedin1")
                .instagramUrl("insta1")
                .tiktokUrl("tiktok1")
                .status(ACTIVE)
                .createdAt(LocalDateTime.of(2025, 7, 8, 3, 2, 1, 4))
                .updatedAt(LocalDateTime.of(2025, 8, 8, 3, 2, 1, 4))
                .build()

        def contact2 = ContactResponse.builder()
                .id(2L)
                .address("address 2")
                .mapUrl("mapUrl 2")
                .email("email2@gmail.com")
                .phoneNumber("+994502222222")
                .linkedinUrl("linkedin2")
                .instagramUrl("insta2")
                .tiktokUrl("tiktok2")
                .status(ACTIVE)
                .createdAt(LocalDateTime.of(2025, 7, 9, 3, 2, 1, 4))
                .updatedAt(LocalDateTime.of(2025, 8, 9, 3, 2, 1, 4))
                .build()

        def responses = [contact1, contact2]

        def objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS)

        def expectedJson = objectMapper.writeValueAsString(responses)


        when:
        def jsonResponse = mockMvc.perform(
                get(url)
                        .contentType(APPLICATION_JSON)
        ).andReturn()

        then:
        1 * contactService.getContacts() >> responses
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(expectedJson.toString(), jsonResponse.response.contentAsString.toString(), true)
    }

    // updateContact
    def "TestUpdateContact success case"() {
        given:
        def id = 1L
        def url = "/v1/contacts/$id"
        def request = UpdateContactRequest.builder()
                .address("address")
                .mapUrl("mapUrl")
                .email("hamburgAcademy@gmail.com")
                .phoneNumber("+994503044665")
                .linkedinUrl("linkedinUrl")
                .instagramUrl("instagramUrl")
                .tiktokUrl("tiktokUrl")
                .build()

        def jsonContactRequest =
                """
                   {
                       "address": "address",
                       "mapUrl": "mapUrl",
                       "email": "hamburgAcademy@gmail.com",
                       "phoneNumber": "+994503044665",
                       "linkedinUrl": "linkedinUrl",
                       "instagramUrl": "instagramUrl",
                       "tiktokUrl": "tiktokUrl"
                   }
                """

        when:
        mockMvc.perform(
                put(url)
                        .content(jsonContactRequest)
                        .contentType(APPLICATION_JSON)
        ).andReturn()

        then:
        1 * contactService.updateContact(id, request)
    }

    // deleteContact
    def "TestDeleteContact"() {
        given:
        def id = 1L
        def url = "/v1/contacts/$id"

        when:
        mockMvc.perform(
                delete(url)
                        .contentType(APPLICATION_JSON)
        ).andReturn()

        then:
        1 * contactService.deleteContact(id)
    }

}
