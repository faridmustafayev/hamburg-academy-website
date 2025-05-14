package az.hamburgit.hamburg.academy.website.service

import az.hamburg.it.hamburg.academy.website.dao.entity.SyllabusEntity
import az.hamburg.it.hamburg.academy.website.dao.repository.SyllabusRepository
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateSyllabusRequest
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService
import az.hamburg.it.hamburg.academy.website.service.abstraction.SyllabusService
import az.hamburg.it.hamburg.academy.website.service.concrete.SyllabusServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.SyllabusMapper.SYLLABUS_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS
import static az.hamburg.it.hamburg.academy.website.util.FileUtils.extractFileNameFromUrl

class SyllabusServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    SyllabusRepository syllabusRepository
    SyllabusService syllabusService
    MinioService minioService

    def setup() {
        syllabusRepository = Mock()
        minioService = Mock()
        syllabusService = new SyllabusServiceHandler(syllabusRepository, minioService)
    }

    // getSyllabus
    def "TestGetSyllabus success case"() {
        given:
        def id = random.nextObject(Long)
        def syllabusEntity = random.nextObject(SyllabusEntity)

        when:
        def syllabusResponse = syllabusService.getSyllabus(id)

        then:
        1 * syllabusRepository.findById(id) >> Optional.of(syllabusEntity)

        syllabusResponse.id == syllabusEntity.id
        syllabusResponse.imagePath == syllabusEntity.imagePath
        syllabusResponse.pdfPath == syllabusEntity.pdfPath
        syllabusResponse.status == syllabusEntity.status
        syllabusResponse.createdAt == syllabusEntity.createdAt
        syllabusResponse.updatedAt == syllabusEntity.updatedAt
    }

    def "TestGetSyllabus SyllabusNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        syllabusService.getSyllabus(id)

        then:
        1 * syllabusRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "SYLLABUS_NOT_FOUND"
        ex.getMessage() == "No syllabus with id (ID: %s) was found".formatted(id)
    }

    // getSyllabuses
    def "TestGetSyllabuses"() {
        given:
        def syllabusEntity = random.nextObject(SyllabusEntity)
        def syllabuses = [syllabusEntity]
        def syllabusResponse = SYLLABUS_MAPPER.buildSyllabusResponse(syllabusEntity)

        when:
        def syllabusResponses = syllabusService.getSyllabuses()

        then:
        1 * syllabusRepository.findAll() >> syllabuses

        syllabusResponse.id == syllabusEntity.id
        syllabusResponse.imagePath == syllabusEntity.imagePath
        syllabusResponse.pdfPath == syllabusEntity.pdfPath
        syllabusResponse.status == syllabusEntity.status
        syllabusResponse.createdAt == syllabusEntity.createdAt
        syllabusResponse.updatedAt == syllabusEntity.updatedAt

        syllabusResponses == [syllabusResponse]
    }

    // updateSyllabus
    def "TestUpdateSyllabus image and pdf not null case"() {
        given:
        def imageFile = new MockMultipartFile(
                "imageFile",
                "syllabus.pdf",
                "image/png",
                "test-content".bytes
        )
        def pdfFile = new MockMultipartFile(
                "pdfFile",
                "syllabus.pdf",
                "image/pdf",
                "test-content".bytes
        )

        def id = random.nextObject(Long)
        def syllabusEntity = random.nextObject(SyllabusEntity)
        syllabusEntity.setImagePath("https://your-bucket.com/path/to/image.pdf")

        def imageFileName = extractFileNameFromUrl(syllabusEntity.imagePath)
        def pdfFileName = extractFileNameFromUrl(syllabusEntity.pdfPath)

        def updateSyllabusRequest = new UpdateSyllabusRequest()
        updateSyllabusRequest.setImageFile(imageFile)
        updateSyllabusRequest.setPdfFile(pdfFile)

        def expectedBucket = "syllabuses"
        ReflectionTestUtils.setField(syllabusService, "syllabusBucket", expectedBucket)

        when:
        syllabusService.updateSyllabus(id, updateSyllabusRequest)

        then:
        1 * syllabusRepository.findById(id) >> Optional.of(syllabusEntity)

        1 * minioService.deleteFile(imageFileName, expectedBucket)
        1 * minioService.uploadFile(imageFile, expectedBucket) >> "uploaded-bucket"
        1 * minioService.getFileUrl("uploaded-bucket", expectedBucket) >> "https://your-bucket.com/path/to/image.pdf"

        1 * minioService.deleteFile(pdfFileName, expectedBucket)
        1 * minioService.uploadFile(pdfFile, expectedBucket) >> "uploaded-bucket"
        1 * minioService.getFileUrl("uploaded-bucket", expectedBucket) >> "https://your-bucket.com/path/to/image.pdf"

        IN_PROGRESS == syllabusEntity.status

        1 * syllabusRepository.save(syllabusEntity)
    }

    def "TestUpdateSyllabus SyllabusNotFound case"() {
        given:
        def imageFile = new MockMultipartFile(
                "imageFile",
                "syllabus.pdf",
                "image/png",
                "test-content".bytes
        )
        def pdfFile = new MockMultipartFile(
                "pdfFile",
                "syllabus.pdf",
                "image/pdf",
                "test-content".bytes
        )

        def id = random.nextObject(Long)
        def syllabusEntity = random.nextObject(SyllabusEntity)
        syllabusEntity.setImagePath("https://your-bucket.com/path/to/image.pdf")

        def updateSyllabusRequest = new UpdateSyllabusRequest()
        updateSyllabusRequest.setImageFile(imageFile)
        updateSyllabusRequest.setPdfFile(pdfFile)

        def expectedBucket = "syllabuses"
        ReflectionTestUtils.setField(syllabusService, "syllabusBucket", expectedBucket)

        when:
        syllabusService.updateSyllabus(id, updateSyllabusRequest)

        then:
        1 * syllabusRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "SYLLABUS_NOT_FOUND"
        ex.getMessage() == "No syllabus with id (ID: %s) was found".formatted(id)

        0 * minioService.deleteFile(_, _)
        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)

        0 * minioService.deleteFile(_, _)
        0 * minioService.uploadFile(_, _)
        0 * minioService.getFileUrl(_, _)

        0 * syllabusRepository.save(_)
    }

    // deleteSyllabus
    def "TestDeleteSyllabus image and pdf not null"() {
        given:
        def id = random.nextObject(Long)
        def syllabusEntity = random.nextObject(SyllabusEntity)
        syllabusEntity.setImagePath("https://your-bucket.com/path/to/image.png")
        syllabusEntity.setPdfPath("https://your-bucket.com/path/to/image.pdf")

        def imageFileName = extractFileNameFromUrl(syllabusEntity.imagePath)
        def pdfFileName = extractFileNameFromUrl(syllabusEntity.pdfPath)

        def expectedBucket = "syllabuses"
        ReflectionTestUtils.setField(syllabusService, "syllabusBucket", expectedBucket)

        when:
        syllabusService.deleteSyllabus(id)

        then:
        1 * syllabusRepository.findById(id) >> Optional.of(syllabusEntity)

        1 * minioService.deleteFile(imageFileName, expectedBucket)
        1 * minioService.deleteFile(pdfFileName, expectedBucket)

        1 * syllabusRepository.deleteByCustomId(syllabusEntity.id)
    }

    def "TestDeleteSyllabus SyllabusNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        syllabusService.deleteSyllabus(id)

        then:
        1 * syllabusRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "SYLLABUS_NOT_FOUND"
        ex.getMessage() == "No syllabus with id (ID: %s) was found".formatted(id)

        0 * minioService.deleteFile(_, _)
        0 * minioService.deleteFile(_, _)

        0 * syllabusRepository.deleteByCustomId(_)
    }

}
