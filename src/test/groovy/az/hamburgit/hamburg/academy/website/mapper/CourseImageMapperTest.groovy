package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.CourseImageEntity
import az.hamburg.it.hamburg.academy.website.model.request.CreateOrUpdateCourseImageRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.CourseImageMapper.COURSE_IMAGE_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE

class CourseImageMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    MultipartFile multipartFile

    def setup() {
        multipartFile = Mock()
    }

    // buildCourseImageEntity
    def "TestBuildCourseImageEntity"() {
        given:
        def mockFile = multipartFile
        def request = new CreateOrUpdateCourseImageRequest()
        request.setImageFile(mockFile)

        when:
        def courseImageEntity = COURSE_IMAGE_MAPPER.buildCourseImageEntity(request)

        then:
        ACTIVE == courseImageEntity.status
    }

    // buildCourseImageResponse
    def "TestBuildCourseImageResponse"() {
        given:
        def courseImageEntity = random.nextObject(CourseImageEntity)

        when:
        def courseImageResponse = COURSE_IMAGE_MAPPER.buildCourseImageResponse(courseImageEntity)

        then:
        courseImageEntity.id == courseImageResponse.id
        courseImageEntity.imagePath == courseImageResponse.imagePath
        courseImageEntity.status == courseImageResponse.status
        courseImageEntity.createdAt == courseImageResponse.createdAt
        courseImageEntity.updatedAt == courseImageResponse.updatedAt
    }

}
