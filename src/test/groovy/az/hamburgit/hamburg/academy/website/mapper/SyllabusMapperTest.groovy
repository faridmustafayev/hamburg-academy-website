package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.SyllabusEntity
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.SyllabusMapper.SYLLABUS_MAPPER

class SyllabusMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // buildSyllabusResponse
    def "TestBuildSyllabusResponse"() {
        given:
        def syllabusEntity = random.nextObject(SyllabusEntity)

        when:
        def syllabusResponse = SYLLABUS_MAPPER.buildSyllabusResponse(syllabusEntity)

        then:
        syllabusResponse.id == syllabusEntity.id
        syllabusResponse.imagePath == syllabusEntity.imagePath
        syllabusResponse.pdfPath == syllabusEntity.pdfPath
        syllabusResponse.status == syllabusEntity.status
        syllabusResponse.createdAt == syllabusEntity.createdAt
        syllabusResponse.updatedAt == syllabusEntity.updatedAt
    }
}
