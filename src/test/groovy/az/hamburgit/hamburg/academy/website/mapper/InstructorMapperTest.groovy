package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.InstructorEntity
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateInstructorRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateInstructorRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.InstructorMapper.INSTRUCTOR_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class InstructorMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // buildInstructorEntity
    def "TestBuildInstructorEntity"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "instructor.png",
                "image/png",
                "test-content".bytes
        )
        def request = new CreateInstructorRequest()
        def specializationEntity = random.nextObject(SpecializationEntity)
        request.setImageFile(mockFile)
        request.setSpecializationId(specializationEntity.id)

        when:
        def instructorEntity = INSTRUCTOR_MAPPER.buildInstructorEntity(request, specializationEntity)

        then:
        request.fullName == instructorEntity.fullName
        request.info == instructorEntity.info
        request.linkedin == instructorEntity.linkedin
        specializationEntity == instructorEntity.specialization
        request.imageFile.getOriginalFilename() == instructorEntity.imagePath
        ACTIVE == instructorEntity.status
    }

    // buildInstructorResponse
    def "TestBuildInstructorResponse"() {
        given:
        def instructorEntity = random.nextObject(InstructorEntity)

        when:
        def instructorResponse = INSTRUCTOR_MAPPER.buildInstructorResponse(instructorEntity)

        then:
        instructorEntity.id == instructorResponse.id
        instructorEntity.fullName == instructorResponse.fullName
        instructorEntity.info == instructorResponse.info
        instructorEntity.linkedin == instructorResponse.linkedin
        instructorEntity.imagePath == instructorResponse.imagePath
        instructorEntity.status == instructorResponse.status
        instructorEntity.createdAt == instructorResponse.createdAt
        instructorEntity.updatedAt == instructorResponse.updatedAt
        instructorEntity.specialization.id == instructorResponse.specializationId
    }

    // updateInstructor
    def "TestUpdateInstructor"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "instructor.png",
                "image/png",
                "test-content".bytes
        )
        def request = new UpdateInstructorRequest()
        def instructorEntity = random.nextObject(InstructorEntity)
        request.setImageFile(mockFile)
        request.setFullName(instructorEntity.fullName)
        request.setLinkedin(instructorEntity.linkedin)
        request.setInfo(instructorEntity.info)
        request.setSpecializationId(instructorEntity.specialization.id)

        when:
        INSTRUCTOR_MAPPER.updateInstructor(instructorEntity, request)

        then:
        instructorEntity.fullName == request.fullName
        instructorEntity.info == request.info
        instructorEntity.linkedin == request.linkedin
        instructorEntity.specialization.id == request.specializationId
        instructorEntity.imagePath == request.imageFile.getOriginalFilename()
        instructorEntity.status == IN_PROGRESS
    }
}
