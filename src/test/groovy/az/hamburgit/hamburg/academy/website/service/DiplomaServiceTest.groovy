package az.hamburgit.hamburg.academy.website.service

import az.hamburg.it.hamburg.academy.website.dao.entity.DiplomaEntity
import az.hamburg.it.hamburg.academy.website.dao.entity.GraduateEntity
import az.hamburg.it.hamburg.academy.website.dao.repository.DiplomaRepository
import az.hamburg.it.hamburg.academy.website.dao.repository.GraduateRepository
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateDiplomaRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateDiplomaRequest
import az.hamburg.it.hamburg.academy.website.service.abstraction.DiplomaService
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService
import az.hamburg.it.hamburg.academy.website.service.concrete.DiplomaServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.DiplomaMapper.DIPLOMA_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS
import static az.hamburg.it.hamburg.academy.website.util.FileUtils.extractFileNameFromUrl

class DiplomaServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    DiplomaService diplomaService
    DiplomaRepository diplomaRepository
    GraduateRepository graduateRepository
    MinioService minioService

    def setup() {
        diplomaRepository = Mock()
        graduateRepository = Mock()
        minioService = Mock()
        diplomaService = new DiplomaServiceHandler(diplomaRepository, graduateRepository, minioService)
    }

    // saveDiploma
    def "TestSaveDiploma image not null case"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "diploma.png",
                "image/png",
                "test-content".bytes
        )
        def createDiplomaRequest = new CreateDiplomaRequest()
        def graduateEntity = random.nextObject(GraduateEntity)

        def diplomaEntity = DIPLOMA_MAPPER.buildDiplomaEntity(createDiplomaRequest, graduateEntity)
        diplomaEntity.setDiplomaPath("https://your-bucket.com/path/to/image.jpg")

        createDiplomaRequest.setImageFile(mockFile)
        createDiplomaRequest.setDiplomaNumber(diplomaEntity.diplomaNumber)
        createDiplomaRequest.setGraduateId(diplomaEntity.graduate.id)

        def expectedBucket = "diplomas"
        ReflectionTestUtils.setField(diplomaService, "diplomaBucket", expectedBucket)

        when:
        diplomaService.saveDiploma(createDiplomaRequest)

        then:
        1 * graduateRepository.findById(createDiplomaRequest.graduateId) >> Optional.of(graduateEntity)
        1 * minioService.uploadFile(mockFile, expectedBucket) >> "uploaded-bucket"
        1 * minioService.getFileUrl("uploaded-bucket", expectedBucket) >> "https://your-bucket.com/new-image.jpg"

        ACTIVE == diplomaEntity.status
        createDiplomaRequest.graduateId == diplomaEntity.graduate.id
        createDiplomaRequest.diplomaNumber == diplomaEntity.diplomaNumber
        "https://your-bucket.com/path/to/image.jpg" == diplomaEntity.diplomaPath

        1 * diplomaRepository.save(diplomaEntity)
    }

    def "TestSaveDiploma image null case"() {
        given:
        def createDiplomaRequest = new CreateDiplomaRequest()
        def graduateEntity = random.nextObject(GraduateEntity)
        def diplomaEntity = DIPLOMA_MAPPER.buildDiplomaEntity(createDiplomaRequest, graduateEntity)

        createDiplomaRequest.setImageFile(null)
        createDiplomaRequest.setGraduateId(diplomaEntity.graduate.id)
        createDiplomaRequest.setDiplomaNumber(diplomaEntity.diplomaNumber)

        when:
        diplomaService.saveDiploma(createDiplomaRequest)

        then:
        1 * graduateRepository.findById(createDiplomaRequest.graduateId) >> Optional.of(graduateEntity)
        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)

        createDiplomaRequest.graduateId == diplomaEntity.graduate.id
        createDiplomaRequest.diplomaNumber == diplomaEntity.diplomaNumber

        1 * diplomaRepository.save(diplomaEntity)
    }

    def "TestSaveDiploma GraduateNotFound case"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "diploma.png",
                "image/png",
                "test-content".bytes
        )
        def createDiplomaRequest = new CreateDiplomaRequest()
        def graduateEntity = random.nextObject(GraduateEntity)

        def diplomaEntity = DIPLOMA_MAPPER.buildDiplomaEntity(createDiplomaRequest, graduateEntity)
        diplomaEntity.setDiplomaPath("https://your-bucket.com/path/to/image.jpg")

        createDiplomaRequest.setImageFile(mockFile)
        createDiplomaRequest.setDiplomaNumber(diplomaEntity.diplomaNumber)
        createDiplomaRequest.setGraduateId(diplomaEntity.graduate.id)

        def expectedBucket = "diplomas"
        ReflectionTestUtils.setField(diplomaService, "diplomaBucket", expectedBucket)

        when:
        diplomaService.saveDiploma(createDiplomaRequest)

        then:
        1 * graduateRepository.findById(createDiplomaRequest.graduateId) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "GRADUATE_NOT_FOUND"
        ex.getMessage() == "No graduate with id (ID: %s) was found".formatted(createDiplomaRequest.graduateId)

        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)
        0 * diplomaRepository.save(_)
    }

    // deleteDiploma
    def "TestDeleteDiploma diploma path not null case"() {
        given:
        def id = random.nextObject(Long)
        def diplomaEntity = random.nextObject(DiplomaEntity)
        diplomaEntity.setDiplomaPath("https://your-bucket.com/path/to/image.jpg")

        def fileName = extractFileNameFromUrl(diplomaEntity.diplomaPath)

        def expectedBucket = "diplomas"
        ReflectionTestUtils.setField(diplomaService, "diplomaBucket", expectedBucket)

        when:
        diplomaService.deleteDiploma(id)

        then:
        1 * diplomaRepository.findById(id) >> Optional.of(diplomaEntity)
        1 * minioService.deleteFile(fileName, expectedBucket)
        1 * diplomaRepository.deleteByIdCustom(id)
    }

    def "TestDeleteDiploma diploma path null case"() {
        given:
        def id = random.nextObject(Long)
        def diplomaEntity = random.nextObject(DiplomaEntity)
        diplomaEntity.setDiplomaPath(null)

        when:
        diplomaService.deleteDiploma(id)

        then:
        1 * diplomaRepository.findById(id) >> Optional.of(diplomaEntity)
        0 * minioService.deleteFile(_, _)
        1 * diplomaRepository.deleteByIdCustom(id)
    }

    def "TestDeleteDiploma DiplomaNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        diplomaService.deleteDiploma(id)

        then:
        1 * diplomaRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "DIPLOMA_NOT_FOUND"
        ex.getMessage() == "No diploma with id (ID: %s) was found".formatted(id)

        0 * minioService.deleteFile(_, _)
        0 * diplomaRepository.deleteByIdCustom(_)
    }

    // updateDiploma
    def "TestUpdateDiploma image not null case"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "diploma.png",
                "image/png",
                "test-content".bytes
        )
        def id = random.nextObject(Long)
        def updateDiplomaRequest = new UpdateDiplomaRequest()

        def diplomaEntity = random.nextObject(DiplomaEntity)
        diplomaEntity.setDiplomaPath("https://your-bucket.com/path/to/image.jpg")

        updateDiplomaRequest.setImageFile(mockFile)
        updateDiplomaRequest.setDiplomaNumber(diplomaEntity.diplomaNumber)
        updateDiplomaRequest.setGraduateId(diplomaEntity.graduate.id)

        def fileName = extractFileNameFromUrl(diplomaEntity.diplomaPath)

        def expectedBucket = "diplomas"
        ReflectionTestUtils.setField(diplomaService, "diplomaBucket", expectedBucket)

        when:
        diplomaService.updateDiploma(id, updateDiplomaRequest)

        then:
        1 * diplomaRepository.findById(id) >> Optional.of(diplomaEntity)

        1 * minioService.deleteFile(fileName, expectedBucket)
        1 * minioService.uploadFile(mockFile, expectedBucket) >> "uploaded-bucket"
        1 * minioService.getFileUrl("uploaded-bucket", expectedBucket) >> "https://your-bucket.com/path/to/image.jpg"

        updateDiplomaRequest.diplomaNumber == diplomaEntity.diplomaNumber
        updateDiplomaRequest.graduateId == diplomaEntity.graduate.id
        IN_PROGRESS == diplomaEntity.status
        "https://your-bucket.com/path/to/image.jpg" == diplomaEntity.diplomaPath


        1 * diplomaRepository.save(diplomaEntity)
    }

    def "TestUpdateDiploam image null case"() {
        given:
        def id = random.nextObject(Long)
        def diplomaEntity = random.nextObject(DiplomaEntity)
        def updateDiplomaRequest = new UpdateDiplomaRequest()
        updateDiplomaRequest.setImageFile(null)
        updateDiplomaRequest.setGraduateId(diplomaEntity.graduate.id)
        updateDiplomaRequest.setDiplomaNumber(diplomaEntity.diplomaNumber)

        when:
        diplomaService.updateDiploma(id, updateDiplomaRequest)

        then:
        1 * diplomaRepository.findById(id) >> Optional.of(diplomaEntity)

        0 * minioService.deleteFile(_, _)
        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)

        updateDiplomaRequest.diplomaNumber == diplomaEntity.diplomaNumber
        updateDiplomaRequest.graduateId == diplomaEntity.graduate.id
        IN_PROGRESS == diplomaEntity.status

        1 * diplomaRepository.save(diplomaEntity)
    }

    def "TestUpdateDiploma DiplomaNotFound case"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "diploma.png",
                "image/png",
                "test-content".bytes
        )
        def id = random.nextObject(Long)
        def updateDiplomaRequest = new UpdateDiplomaRequest()

        def diplomaEntity = random.nextObject(DiplomaEntity)
        diplomaEntity.setDiplomaPath("https://your-bucket.com/path/to/image.jpg")

        updateDiplomaRequest.setImageFile(mockFile)
        updateDiplomaRequest.setDiplomaNumber(diplomaEntity.diplomaNumber)
        updateDiplomaRequest.setGraduateId(diplomaEntity.graduate.id)

        def expectedBucket = "diplomas"
        ReflectionTestUtils.setField(diplomaService, "diplomaBucket", expectedBucket)

        when:
        diplomaService.updateDiploma(id, updateDiplomaRequest)

        then:
        1 * diplomaRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "DIPLOMA_NOT_FOUND"
        ex.getMessage() == "No diploma with id (ID: %s) was found".formatted(id)

        0 * minioService.deleteFile(_, _)
        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)

        0 * diplomaRepository.save(_)
    }

    // getDiploma
    def "TestGetDiploma success case"() {
        given:
        def id = random.nextObject(Long)
        def diplomaEntity = random.nextObject(DiplomaEntity)

        when:
        def diplomaResponse = diplomaService.getDiploma(id)

        then:
        1 * diplomaRepository.findById(id) >> Optional.of(diplomaEntity)
        diplomaResponse.id == diplomaEntity.id
        diplomaResponse.diplomaPath == diplomaEntity.diplomaPath
        diplomaResponse.diplomaNumber == diplomaEntity.diplomaNumber
        diplomaResponse.status == diplomaEntity.status
        diplomaResponse.createdAt == diplomaEntity.createdAt
        diplomaResponse.updatedAt == diplomaEntity.updatedAt
    }

    def "TestGetDiploma DiplomaNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        diplomaService.getDiploma(id)

        then:
        1 * diplomaRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "DIPLOMA_NOT_FOUND"
        ex.getMessage() == "No diploma with id (ID: %s) was found".formatted(id)
    }

    // getDiplomas
    def "TestGetDiplomas"() {
        given:
        def diplomaEntity = random.nextObject(DiplomaEntity)
        def diplomas = [diplomaEntity]
        def diplomaResponse = DIPLOMA_MAPPER.buildDiplomaResponse(diplomaEntity)

        when:
        def diplomaResponses = diplomaService.getDiplomas()

        then:
        1 * diplomaRepository.findAll() >> diplomas

        diplomaResponse.id == diplomaEntity.id
        diplomaResponse.diplomaNumber == diplomaEntity.diplomaNumber
        diplomaResponse.diplomaPath == diplomaEntity.diplomaPath
        diplomaResponse.createdAt == diplomaEntity.createdAt
        diplomaResponse.status == diplomaEntity.status
        diplomaResponse.updatedAt == diplomaEntity.updatedAt

        diplomaResponses == [diplomaResponse]
    }

}
