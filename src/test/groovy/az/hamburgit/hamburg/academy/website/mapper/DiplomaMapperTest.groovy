package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.DiplomaEntity
import az.hamburg.it.hamburg.academy.website.dao.entity.GraduateEntity
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateDiplomaRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateDiplomaRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.DiplomaMapper.DIPLOMA_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class DiplomaMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    MultipartFile multipartFile

    def setup() {
        multipartFile = Mock()
    }

    // buildDiplomaEntity
    def "TestBuildDiplomaEntity"() {
        given:
        def graduateEntity = random.nextObject(GraduateEntity)
        def mockFile = multipartFile
        def request = new CreateDiplomaRequest()
        request.setImageFile(mockFile)
        request.setGraduateId(graduateEntity.getId())

        when:
        def diplomaEntity = DIPLOMA_MAPPER.buildDiplomaEntity(request, graduateEntity)

        then:
        graduateEntity == diplomaEntity.graduate
        request.diplomaNumber == request.diplomaNumber
        request.graduateId == diplomaEntity.graduate.id
        ACTIVE == diplomaEntity.status
    }

    // buildDiplomaResponse
    def "TestBuildDiplomaResponse"() {
        given:
        def diplomaEntity = random.nextObject(DiplomaEntity)

        when:
        def diplomaResponse = DIPLOMA_MAPPER.buildDiplomaResponse(diplomaEntity)

        then:
        diplomaEntity.id == diplomaResponse.id
        diplomaEntity.diplomaPath == diplomaResponse.diplomaPath
        diplomaEntity.diplomaNumber == diplomaResponse.diplomaNumber
        diplomaEntity.status == diplomaResponse.status
        diplomaEntity.createdAt == diplomaResponse.createdAt
        diplomaEntity.updatedAt == diplomaResponse.updatedAt
    }

    // updateDiploma
    def "TestUpdateDiploma"() {
        given:
        def diplomaEntity = random.nextObject(DiplomaEntity)

        def mockFile = new MockMultipartFile(
                "imageFile",
                "diploma.png",
                "image/png",
                "test-content".bytes
        )

        def request = new UpdateDiplomaRequest()
        request.setImageFile(mockFile)
        request.setDiplomaNumber(diplomaEntity.diplomaNumber)
        request.setGraduateId(diplomaEntity.graduate.id)

        when:
        DIPLOMA_MAPPER.updateDiploma(diplomaEntity, request)

        then:
        diplomaEntity.diplomaNumber == request.diplomaNumber
        diplomaEntity.graduate.id == request.graduateId
        diplomaEntity.status == IN_PROGRESS
    }
}
