package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.GraduateEntity
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateGraduateRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateGraduateRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.DiplomaMapper.DIPLOMA_MAPPER
import static az.hamburg.it.hamburg.academy.website.mapper.GraduateMapper.GRADUATE_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class GraduateMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // buildGraduateEntity
    def "TestBuildGraduateEntity"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "diploma.png",
                "image/png",
                "test-content".bytes
        )

        def request = new CreateGraduateRequest()
        request.setImageFile(mockFile)
        def specializations = random.nextObject(List<SpecializationEntity>)

        when:
        def graduateEntity = GRADUATE_MAPPER.buildGraduateEntity(request, specializations)

        then:
        specializations == graduateEntity.specializations
        request.fullName == graduateEntity.fullName
        request.linkedin == graduateEntity.linkedin
        request.category == graduateEntity.category
        request.imageFile.originalFilename == graduateEntity.imagePath
        ACTIVE == graduateEntity.status
    }

    // buildGraduateResponse
    def "TestBuildGraduateResponse"() {
        given:
        def graduateEntity = random.nextObject(GraduateEntity)

        when:
        def graduateResponse = GRADUATE_MAPPER.buildGraduateResponse(graduateEntity)

        then:
        graduateEntity.id == graduateResponse.id
        graduateEntity.fullName == graduateResponse.fullName
        graduateEntity.imagePath == graduateResponse.imagePath
        graduateEntity.linkedin == graduateResponse.linkedin
        graduateEntity.category == graduateResponse.category
        graduateEntity.status == graduateResponse.status
        graduateEntity.createdAt == graduateResponse.createdAt
        graduateEntity.updatedAt == graduateResponse.updatedAt

        graduateEntity.diplomas.stream()
                .map(DIPLOMA_MAPPER::buildDiplomaResponse)
                .toList() == graduateResponse.diplomas

        graduateEntity.specializations.stream()
                .map(SpecializationEntity::getId)
                .toList() == graduateResponse.specializationIds
    }

    // updateGraduate
    def "TestUpdateGraduate"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "graduate.png",
                "image/png",
                "test-content".bytes
        )
        def graduateEntity = random.nextObject(GraduateEntity)
        def request = new UpdateGraduateRequest()
        request.setImageFile(mockFile)
        request.setFullName(graduateEntity.fullName)
        request.setLinkedin(graduateEntity.linkedin)
        request.setCategory(graduateEntity.category)
        request.setSpecializationIds(Arrays.asList(1L, 2L))

        when:
        GRADUATE_MAPPER.updateGraduate(graduateEntity, request)

        then:
        graduateEntity.fullName == request.fullName
        graduateEntity.linkedin == request.linkedin
        graduateEntity.category == request.category
        graduateEntity.status == IN_PROGRESS
        graduateEntity.imagePath == request.imageFile.getOriginalFilename()

        def actualIds = graduateEntity.getSpecializations().stream()
                .map(SpecializationEntity::getId)
                .toList()
        def expectedIds = request.getSpecializationIds()

        assert expectedIds != null
        assert actualIds == expectedIds
    }
}
