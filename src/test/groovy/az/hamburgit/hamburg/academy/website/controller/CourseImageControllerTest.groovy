package az.hamburgit.hamburg.academy.website.controller

import az.hamburg.it.hamburg.academy.website.controller.CourseImageController
import az.hamburg.it.hamburg.academy.website.exception.ErrorHandler
import az.hamburg.it.hamburg.academy.website.model.request.CreateOrUpdateCourseImageRequest
import az.hamburg.it.hamburg.academy.website.model.response.CourseImageResponse
import az.hamburg.it.hamburg.academy.website.service.abstraction.CourseImageService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime

import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import static org.springframework.http.HttpHeaders.AUTHORIZATION
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

class CourseImageControllerTest extends Specification {
    CourseImageService courseImageService
    CourseImageController courseImageController
    MockMvc mockMvc

    def setup() {
        courseImageService = Mock()
        courseImageController = new CourseImageController(courseImageService)
        mockMvc = MockMvcBuilders.standaloneSetup(courseImageController)
                .setControllerAdvice(ErrorHandler.class)
                .build()
    }

    // saveCourseImage
    def "TestCourseImage"() {
        given:
        def url = "/v1/course_images"

        def mockFile = new MockMultipartFile(
                "imageFile",
                "courseImage.png",
                "image/png",
                "test-content".bytes
        )

        def courseImageRequest = CreateOrUpdateCourseImageRequest.builder()
                .imageFile(mockFile)
                .build()

        when:
        def jsonResponse = mockMvc.perform(
                multipart(url)
                        .file(mockFile)
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .header(AUTHORIZATION, "")
        ).andReturn()

        then:
        1 * courseImageService.saveCourseImage(courseImageRequest)
        jsonResponse.response.status == CREATED.value()
    }

    // getCourseImage
    def "TestGetCourseImage"() {
        given:
        def id = 1L
        def url = "/v1/course_images/$id"

        def courseImageResponse = CourseImageResponse.builder()
                .id(id)
                .imagePath("http.path//def")
                .status(ACTIVE)
                .createdAt(LocalDateTime.of(2025, 4, 3, 2, 1, 4, 5))
                .updatedAt(LocalDateTime.of(2025, 5, 3, 2, 1, 4, 5))
                .build()

        def objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS)
        def expectedJson = objectMapper.writeValueAsString(courseImageResponse)

        when:
        def jsonResponse = mockMvc.perform(
                get(url)
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "")
        ).andReturn()

        then:
        1 * courseImageService.getCourseImage(id) >> courseImageResponse
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(expectedJson.toString(), jsonResponse.response.contentAsString.toString(), true)
    }

    // getCourseImages
    def "TestGetCourseImages"() {
        given:
        def url = "/v1/course_images"

        def courseImageResponse1 = CourseImageResponse.builder()
                .id(1L)
                .imagePath("http.path//def 1")
                .status(ACTIVE)
                .createdAt(LocalDateTime.of(2025, 4, 3, 2, 1, 4, 5))
                .updatedAt(LocalDateTime.of(2025, 5, 3, 2, 1, 4, 5))
                .build()

        def courseImageResponse2 = CourseImageResponse.builder()
                .id(2L)
                .imagePath("http.path//def 2")
                .status(ACTIVE)
                .createdAt(LocalDateTime.of(2025, 6, 3, 2, 1, 4, 5))
                .updatedAt(LocalDateTime.of(2025, 7, 3, 2, 1, 4, 5))
                .build()

        def responses = [courseImageResponse1, courseImageResponse2]

        def objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS)

        def expectedJson = objectMapper.writeValueAsString(responses)

        when:
        def jsonResponse = mockMvc.perform(
                get(url)
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "")
        ).andReturn()

        then:
        1 * courseImageService.getCourseImages() >> responses
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(expectedJson.toString(), jsonResponse.response.contentAsString.toString(), true)
    }

    // updateCourseImage
    def "TestUpdateCourseImage"() {
        given:
        def id = 1L
        def url = "/v1/course_images/$id"

        def mockFile = new MockMultipartFile(
                "imageFile",
                "courseImage.png",
                "image/png",
                "test-content".bytes
        )

        def courseImageRequest = CreateOrUpdateCourseImageRequest.builder()
                .imageFile(mockFile)
                .build()

        when:
        def patchMultipart = { req ->
            req as MockHttpServletRequest
            req.method = "PATCH"
            return req
        } as RequestPostProcessor

        def jsonResponse = mockMvc.perform(
                multipart(url)
                        .file(mockFile)
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .header(AUTHORIZATION, "")
                        .with(patchMultipart)
        ).andReturn()


        then:
        1 * courseImageService.updateCourseImage(id, courseImageRequest)
        jsonResponse.response.status == NO_CONTENT.value()
    }

    // deleteCourseImage
    def "TestDeleteCourseImage"() {
        given:
        def id = 1L
        def url = "/v1/course_images/$id"

        when:
        def jsonResponse = mockMvc.perform(
                delete(url)
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "")
        ).andReturn()

        then:
        1 * courseImageService.deleteCourseImage(id)
        jsonResponse.response.status == NO_CONTENT.value()

    }
}
