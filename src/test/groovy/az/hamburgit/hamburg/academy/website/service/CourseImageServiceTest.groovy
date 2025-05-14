package az.hamburgit.hamburg.academy.website.service

import az.hamburg.it.hamburg.academy.website.dao.entity.CourseImageEntity
import az.hamburg.it.hamburg.academy.website.dao.repository.CourseImageRepository
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException
import az.hamburg.it.hamburg.academy.website.model.request.CreateOrUpdateCourseImageRequest
import az.hamburg.it.hamburg.academy.website.service.abstraction.CourseImageService
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService
import az.hamburg.it.hamburg.academy.website.service.concrete.CourseImageServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.CourseImageMapper.COURSE_IMAGE_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.*
import static az.hamburg.it.hamburg.academy.website.util.FileUtils.extractFileNameFromUrl

class CourseImageServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    CourseImageRepository courseImageRepository
    CourseImageService courseImageService
    MinioService minioService

    def setup() {
        courseImageRepository = Mock()
        minioService = Mock()
        courseImageService = new CourseImageServiceHandler(courseImageRepository, minioService)
    }

    // saveCourseImage
    def "TestSaveCourseImage image not null case"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "course-images.png",
                "image/png",
                "test-content".bytes
        )
        def createOrUpdateCourseImageRequest = new CreateOrUpdateCourseImageRequest()
        createOrUpdateCourseImageRequest.setImageFile(mockFile)

        def courseImageEntity = COURSE_IMAGE_MAPPER.buildCourseImageEntity(createOrUpdateCourseImageRequest)
        courseImageEntity.setImagePath("https://your-bucket.com/path/to/image.jpg")

        def expectedBucket = "course-images"

        ReflectionTestUtils.setField(courseImageService, "courseImageBucket", expectedBucket)

        when:
        courseImageService.saveCourseImage(createOrUpdateCourseImageRequest)

        then:
        1 * minioService.uploadFile(mockFile, expectedBucket) >> "uploaded-object"
        1 * minioService.getFileUrl("uploaded-object", expectedBucket) >> "https://your-bucket.com/new-image.jpg"

        ACTIVE == courseImageEntity.status
        "https://your-bucket.com/path/to/image.jpg" == courseImageEntity.imagePath

        1 * courseImageRepository.save(courseImageEntity)
    }

    def "TestSaveCourseImage image null case"() {
        given:
        def createOrUpdateCourseImageRequest = new CreateOrUpdateCourseImageRequest()
        createOrUpdateCourseImageRequest.setImageFile(null)

        def courseImageEntity = COURSE_IMAGE_MAPPER.buildCourseImageEntity(createOrUpdateCourseImageRequest)
        courseImageEntity.setImagePath(null)

        when:
        courseImageService.saveCourseImage(createOrUpdateCourseImageRequest)

        then:
        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)
        1 * courseImageRepository.save(courseImageEntity)
    }

    // getCourseImage
    def "TestGetCourseImage success case"() {
        given:
        def id = random.nextObject(Long)
        def courseImageEntity = random.nextObject(CourseImageEntity)

        when:
        def courseImageResponse = courseImageService.getCourseImage(id)

        then:
        1 * courseImageRepository.findById(id) >> Optional.of(courseImageEntity)
        courseImageResponse.id == courseImageEntity.id
        courseImageResponse.imagePath == courseImageEntity.imagePath
        courseImageResponse.status == courseImageEntity.status
        courseImageResponse.createdAt == courseImageEntity.createdAt
        courseImageResponse.updatedAt == courseImageEntity.updatedAt
    }

    def "TestGetCourseImage CourseImageNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        courseImageService.getCourseImage(id)

        then:
        1 * courseImageRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "COURSE_IMAGE_NOT_FOUND"
        ex.getMessage() == "No course_image with id (ID: %s) was found".formatted(id)
    }

    // getCourseImages
    def "TestGetCourseImages"() {
        given:
        def courseImageEntity = random.nextObject(CourseImageEntity)
        def courseImages = [courseImageEntity]
        def courseImageResponse = COURSE_IMAGE_MAPPER.buildCourseImageResponse(courseImageEntity)

        when:
        def courseImageResponses = courseImageService.getCourseImages()

        then:
        1 * courseImageRepository.findAll() >> courseImages
        courseImageResponse.id == courseImageEntity.id
        courseImageResponse.imagePath == courseImageEntity.imagePath
        courseImageResponse.status == courseImageEntity.status
        courseImageResponse.createdAt == courseImageEntity.createdAt
        courseImageResponse.updatedAt == courseImageEntity.updatedAt
        [courseImageResponse] == courseImageResponses
    }

    // updateCourseImage
    def "TestUpdateCourseImage image not null case"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "course-images.png",
                "image/png",
                "test-content".bytes
        )
        def id = random.nextObject(Long)
        def createOrUpdateImageRequest = new CreateOrUpdateCourseImageRequest()
        createOrUpdateImageRequest.setImageFile(mockFile)

        def courseImageEntity = random.nextObject(CourseImageEntity)
        courseImageEntity.setImagePath("https://your-bucket.com/path/to/image.jpg")

        def expectedBucket = "course-images"
        def fileName = extractFileNameFromUrl(courseImageEntity.imagePath)

        ReflectionTestUtils.setField(courseImageService, "courseImageBucket", expectedBucket)

        when:
        courseImageService.updateCourseImage(id, createOrUpdateImageRequest)

        then:
        1 * courseImageRepository.findById(id) >> Optional.of(courseImageEntity)
        1 * minioService.deleteFile(fileName, expectedBucket)
        1 * minioService.uploadFile(mockFile, expectedBucket) >> "uploaded-object"
        1 * minioService.getFileUrl("uploaded-object", expectedBucket) >> "https://your-bucket.com/new-image.jpg"

        "https://your-bucket.com/new-image.jpg" == courseImageEntity.imagePath
        IN_PROGRESS == courseImageEntity.status

        1 * courseImageRepository.save(courseImageEntity)
    }

    def "TestUpdateCourseImage image null case"() {
        given:
        def id = random.nextObject(Long)
        def createOrUpdateCourseImageRequest = new CreateOrUpdateCourseImageRequest()
        createOrUpdateCourseImageRequest.setImageFile(null)

        def courseImageEntity = random.nextObject(CourseImageEntity)
        courseImageEntity.setImagePath(null)

        when:
        courseImageService.updateCourseImage(id, createOrUpdateCourseImageRequest)

        then:
        1 * courseImageRepository.findById(id) >> Optional.of(courseImageEntity)
        0 * minioService.deleteFile(_, _)
        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)

        IN_PROGRESS == courseImageEntity.status

        1 * courseImageRepository.save(courseImageEntity)
    }

    // deleteCourseImage
    def "TestDeleteCourseImage success case"() {
        given:
        def id = random.nextObject(Long)
        def courseImageEntity = random.nextObject(CourseImageEntity)

        when:
        courseImageService.deleteCourseImage(id)

        then:
        1 * courseImageRepository.findById(id) >> Optional.of(courseImageEntity)

        DELETED == courseImageEntity.status

        1 * courseImageRepository.save(courseImageEntity)
    }

    def "TestDeleteCourseImage CourseImageNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        courseImageService.deleteCourseImage(id)

        then:
        1 * courseImageRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "COURSE_IMAGE_NOT_FOUND"
        ex.getMessage() == "No course_image with id (ID: %s) was found".formatted(id)
    }

}
