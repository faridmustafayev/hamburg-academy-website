package az.hamburgit.hamburg.academy.website.service

import az.hamburg.it.hamburg.academy.website.dao.entity.InstructorEntity
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity
import az.hamburg.it.hamburg.academy.website.dao.repository.InstructorRepository
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateInstructorRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateInstructorRequest
import az.hamburg.it.hamburg.academy.website.service.abstraction.InstructorService
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService
import az.hamburg.it.hamburg.academy.website.service.abstraction.SpecializationService
import az.hamburg.it.hamburg.academy.website.service.concrete.InstructorServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.SPECIALIZATION_NOT_FOUND
import static az.hamburg.it.hamburg.academy.website.mapper.InstructorMapper.INSTRUCTOR_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS
import static az.hamburg.it.hamburg.academy.website.util.FileUtils.extractFileNameFromUrl

class InstructorServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    InstructorService instructorService
    InstructorRepository instructorRepository
    SpecializationService specializationService
    MinioService minioService

    def setup() {
        instructorRepository = Mock()
        specializationService = Mock()
        minioService = Mock()
        instructorService = new InstructorServiceHandler(instructorRepository, specializationService, minioService)
    }

    // saveInstructor
    def "TestSaveInstructor image not null case"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "instructor.png",
                "image/png",
                "test-content".bytes
        )
        def createInstructorRequest = new CreateInstructorRequest()
        def specializationEntity = random.nextObject(SpecializationEntity)
        def instructorEntity = INSTRUCTOR_MAPPER.buildInstructorEntity(createInstructorRequest, specializationEntity)
        instructorEntity.setImagePath("https://your-bucket.com/path/to/image.jpg")

        createInstructorRequest.setImageFile(mockFile)
        createInstructorRequest.setFullName(instructorEntity.fullName)
        createInstructorRequest.setLinkedin(instructorEntity.linkedin)
        createInstructorRequest.setInfo(instructorEntity.info)
        createInstructorRequest.setSpecializationId(instructorEntity.specialization.id)

        def expectedBucket = "instructors"

        ReflectionTestUtils.setField(instructorService, "instructorBucket", expectedBucket)

        when:
        instructorService.saveInstructor(createInstructorRequest)

        then:
        1 * specializationService.fetchSpecializationIfExist(createInstructorRequest.specializationId) >> specializationEntity
        1 * minioService.uploadFile(mockFile, expectedBucket) >> "uploaded-object"
        1 * minioService.getFileUrl("uploaded-object", expectedBucket) >> "https://your-bucket.com/new-image.jpg"

        createInstructorRequest.fullName == instructorEntity.fullName
        createInstructorRequest.info == instructorEntity.info
        createInstructorRequest.linkedin == instructorEntity.linkedin
        createInstructorRequest.specializationId == instructorEntity.specialization.id
        "https://your-bucket.com/path/to/image.jpg" == instructorEntity.imagePath
        ACTIVE == instructorEntity.status
        specializationEntity == instructorEntity.specialization

        1 * instructorRepository.save(instructorEntity)
    }

    def "TestSaveInstructor image null case"() {
        given:
        def createInstructorRequest = new CreateInstructorRequest()
        def specializationEntity = random.nextObject(SpecializationEntity)
        def instructorEntity = INSTRUCTOR_MAPPER.buildInstructorEntity(createInstructorRequest, specializationEntity)
        instructorEntity.setImagePath(null)

        createInstructorRequest.setImageFile(null)
        createInstructorRequest.setSpecializationId(instructorEntity.specialization.id)
        createInstructorRequest.setInfo(instructorEntity.info)
        createInstructorRequest.setLinkedin(instructorEntity.linkedin)
        createInstructorRequest.setFullName(instructorEntity.fullName)

        when:
        instructorService.saveInstructor(createInstructorRequest)

        then:
        1 * specializationService.fetchSpecializationIfExist(createInstructorRequest.specializationId) >> specializationEntity
        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)

        createInstructorRequest.fullName == instructorEntity.fullName
        createInstructorRequest.info == instructorEntity.info
        createInstructorRequest.linkedin == instructorEntity.linkedin
        createInstructorRequest.specializationId == instructorEntity.specialization.id
        ACTIVE == instructorEntity.status

        1 * instructorRepository.save(instructorEntity)
    }

    def "TestSaveInstructor SpecializationNotFound case"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "instructor.png",
                "image/png",
                "test-content".bytes
        )
        def createInstructorRequest = new CreateInstructorRequest()
        createInstructorRequest.setImageFile(mockFile)
        createInstructorRequest.setFullName("John Doe")
        createInstructorRequest.setInfo("Some info")
        createInstructorRequest.setLinkedin("linkedin.com/in/johndoe")
        createInstructorRequest.setSpecializationId(123L)

        def expectedBucket = "instructors"
        ReflectionTestUtils.setField(instructorService, "instructorBucket", expectedBucket)

        when:
        instructorService.saveInstructor(createInstructorRequest)

        then:
        1 * specializationService.fetchSpecializationIfExist(createInstructorRequest.specializationId) >> {
            throw new NotFoundException(SPECIALIZATION_NOT_FOUND.getCode(),
                    "No specializations with id (ID: %s) was found".formatted(123L))
        }

        def ex = thrown(NotFoundException)
        ex.getCode() == "SPECIALIZATION_NOT_FOUND"
        ex.getMessage() == "No specializations with id (ID: 123) was found"

        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)
        0 * instructorRepository.save(_)
    }


    // getInstructor
    def "TestGetInstructor success case"() {
        given:
        def id = random.nextObject(Long)
        def instructorEntity = random.nextObject(InstructorEntity)

        when:
        def instructorResponse = instructorService.getInstructor(id)

        then:
        1 * instructorRepository.findById(id) >> Optional.of(instructorEntity)
        instructorResponse.id == instructorEntity.id
        instructorResponse.fullName == instructorEntity.fullName
        instructorResponse.info == instructorEntity.info
        instructorResponse.linkedin == instructorEntity.linkedin
        instructorResponse.imagePath == instructorEntity.imagePath
        instructorResponse.status == instructorEntity.status
        instructorResponse.createdAt == instructorEntity.createdAt
        instructorResponse.updatedAt == instructorEntity.updatedAt
        instructorResponse.specializationId == instructorEntity.specialization.id
    }

    def "TestGetInstructor InstructorNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        instructorService.getInstructor(id)

        then:
        1 * instructorRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "INSTRUCTOR_NOT_FOUND"
        ex.getMessage() == "No instructor with id (ID: %s) was found".formatted(id)
    }

    // getAll
    def "TestGetAll"() {
        given:
        def instructorEntity = random.nextObject(InstructorEntity)
        def instructors = [instructorEntity]

        when:
        def instructorResponses = instructorService.getAll()
        def instructorResponse = INSTRUCTOR_MAPPER.buildInstructorResponse(instructorEntity)

        then:
        1 * instructorRepository.findAll() >> instructors
        instructorResponse.id == instructorEntity.id
        instructorResponse.fullName == instructorEntity.fullName
        instructorResponse.info == instructorEntity.info
        instructorResponse.linkedin == instructorEntity.linkedin
        instructorResponse.imagePath == instructorEntity.imagePath
        instructorResponse.status == instructorEntity.status
        instructorResponse.createdAt == instructorEntity.createdAt
        instructorResponse.updatedAt == instructorEntity.updatedAt
        instructorResponse.specializationId == instructorEntity.specialization.id
        instructorResponses == [instructorResponse]
    }

    // deleteInstructor
    def "TestDeleteInstructor image not null case"() {
        given:
        def id = random.nextObject(Long)
        def instructorEntity = random.nextObject(InstructorEntity)
        instructorEntity.setImagePath("https://your-bucket.com/path/to/image.jpg")
        def expectedBucket = "instructors"
        def fileName = extractFileNameFromUrl(instructorEntity.imagePath)

        ReflectionTestUtils.setField(instructorService, "instructorBucket", expectedBucket)

        when:
        instructorService.deleteInstructor(id)

        then:
        1 * instructorRepository.findById(id) >> Optional.of(instructorEntity)
        1 * minioService.deleteFile(fileName, expectedBucket)
        1 * instructorRepository.deleteByIdCustom(instructorEntity.id)
    }

    def "TestDeleteInstructor image null case"() {
        given:
        def id = random.nextObject(Long)
        def instructorEntity = random.nextObject(InstructorEntity)
        instructorEntity.setImagePath(null)

        when:
        instructorService.deleteInstructor(id)

        then:
        1 * instructorRepository.findById(id) >> Optional.of(instructorEntity)
        0 * minioService.deleteFile(_, _)
        1 * instructorRepository.deleteByIdCustom(instructorEntity.id)
    }

    def "TestDeleteInstructor InstructorNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        instructorService.deleteInstructor(id)

        then:
        1 * instructorRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "INSTRUCTOR_NOT_FOUND"
        ex.getMessage() == "No instructor with id (ID: %s) was found".formatted(id)
        0 * minioService.deleteFile(_, _)
        0 * instructorRepository.deleteByIdCustom(id)
    }

    // updateInstructor
    def "TestUpdateInstructor image not null case"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "instructor.png",
                "image/png",
                "test-content".bytes
        )
        def id = random.nextObject(Long)

        def instructorEntity = random.nextObject(InstructorEntity)
        instructorEntity.setImagePath("https://your-bucket.com/path/to/image.jpg")

        def updateInstructorRequest = new UpdateInstructorRequest()
        updateInstructorRequest.setImageFile(mockFile)
        updateInstructorRequest.setSpecializationId(instructorEntity.specialization.id)
        updateInstructorRequest.setInfo(instructorEntity.info)
        updateInstructorRequest.setLinkedin(instructorEntity.linkedin)
        updateInstructorRequest.setFullName(instructorEntity.fullName)

        def expectedBucket = "instructors"
        def fileName = extractFileNameFromUrl(instructorEntity.imagePath)

        ReflectionTestUtils.setField(instructorService, "instructorBucket", expectedBucket)

        when:
        instructorService.updateInstructor(id, updateInstructorRequest)

        then:
        1 * instructorRepository.findById(id) >> Optional.of(instructorEntity)
        1 * minioService.deleteFile(fileName, expectedBucket)
        1 * minioService.uploadFile(mockFile, expectedBucket) >> "uploaded-object"
        1 * minioService.getFileUrl("uploaded-object", expectedBucket) >> "https://your-bucket.com/new-image.jpg"

        updateInstructorRequest.fullName == instructorEntity.fullName
        updateInstructorRequest.info == instructorEntity.info
        updateInstructorRequest.linkedin == instructorEntity.linkedin
        updateInstructorRequest.specializationId == instructorEntity.specialization.id
        updateInstructorRequest.imageFile.originalFilename == instructorEntity.imagePath
        IN_PROGRESS == instructorEntity.status

        1 * instructorRepository.save(instructorEntity)
    }

    def "TestUpdateInstructor image null case"() {
        given:
        def id = random.nextObject(Long)
        def instructorEntity = random.nextObject(InstructorEntity)
        instructorEntity.setImagePath(null)

        def updateInstructorRequest = new UpdateInstructorRequest()
        updateInstructorRequest.setImageFile(null)
        updateInstructorRequest.setSpecializationId(instructorEntity.specialization.id)
        updateInstructorRequest.setInfo(instructorEntity.info)
        updateInstructorRequest.setLinkedin(instructorEntity.linkedin)
        updateInstructorRequest.setFullName(instructorEntity.fullName)

        when:
        instructorService.updateInstructor(id, updateInstructorRequest)

        then:
        1 * instructorRepository.findById(id) >> Optional.of(instructorEntity)
        0 * minioService.deleteFile(_, _)
        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)

        updateInstructorRequest.fullName == instructorEntity.fullName
        updateInstructorRequest.info == instructorEntity.info
        updateInstructorRequest.linkedin == instructorEntity.linkedin
        updateInstructorRequest.specializationId == instructorEntity.specialization.id
        IN_PROGRESS == instructorEntity.status

        1 * instructorRepository.save(instructorEntity)
    }

    def "TestUpdateInstructor InstructorNotFound case"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "instructor.png",
                "image/png",
                "test-content".bytes
        )
        def id = random.nextObject(Long)

        def instructorEntity = random.nextObject(InstructorEntity)
        instructorEntity.setImagePath("https://your-bucket.com/path/to/image.jpg")

        def updateInstructorRequest = new UpdateInstructorRequest()
        updateInstructorRequest.setImageFile(mockFile)
        updateInstructorRequest.setSpecializationId(instructorEntity.specialization.id)
        updateInstructorRequest.setInfo(instructorEntity.info)
        updateInstructorRequest.setLinkedin(instructorEntity.linkedin)
        updateInstructorRequest.setFullName(instructorEntity.fullName)

        def expectedBucket = "instructors"
        ReflectionTestUtils.setField(instructorService, "instructorBucket", expectedBucket)

        when:
        instructorService.updateInstructor(id, updateInstructorRequest)

        then:
        1 * instructorRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "INSTRUCTOR_NOT_FOUND"
        ex.getMessage() == "No instructor with id (ID: %s) was found".formatted(id)
        0 * minioService.deleteFile(_, _)
        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)
        0 * instructorRepository.save(instructorEntity)
    }

}
