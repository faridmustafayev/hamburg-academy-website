package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateSpecializationRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import java.util.stream.Collectors

import static az.hamburg.it.hamburg.academy.website.mapper.CourseRequestMapper.COURSE_REQUEST_MAPPER
import static az.hamburg.it.hamburg.academy.website.mapper.GraduateMapper.GRADUATE_MAPPER
import static az.hamburg.it.hamburg.academy.website.mapper.InstructorMapper.INSTRUCTOR_MAPPER
import static az.hamburg.it.hamburg.academy.website.mapper.ReviewMapper.REVIEW_MAPPER
import static az.hamburg.it.hamburg.academy.website.mapper.SpecializationMapper.SPECIALIZATION_MAPPER
import static az.hamburg.it.hamburg.academy.website.mapper.SyllabusMapper.SYLLABUS_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class SpecializationMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // buildSpecializationResponse
    def "TestBuildSpecializationResponse"() {
        given:
        def specializationEntity = random.nextObject(SpecializationEntity)

        when:
        def specializationResponse = SPECIALIZATION_MAPPER.buildSpecializationResponse(specializationEntity)

        then:
        specializationResponse.id == specializationEntity.id
        specializationResponse.name == specializationEntity.name
        specializationResponse.description == specializationEntity.description
        specializationResponse.status == specializationEntity.status
        specializationResponse.createdAt == specializationEntity.createdAt
        specializationResponse.updatedAt == specializationEntity.updatedAt

        specializationResponse.instructors == specializationEntity.getInstructors()
                .stream()
                .map(INSTRUCTOR_MAPPER::buildInstructorResponse)
                .collect(Collectors.toList())

        specializationResponse.reviews == specializationEntity.getReviews().stream()
                .map(REVIEW_MAPPER::buildReviewResponse)
                .collect(Collectors.toList())

        specializationResponse.graduates == specializationEntity.getGraduates().stream()
                .map(GRADUATE_MAPPER::buildGraduateResponse)
                .collect(Collectors.toList())

        specializationResponse.courseRequests == specializationEntity.getCourseRequests().stream()
                .map(COURSE_REQUEST_MAPPER::buildCourseRequestOutput)
                .collect(Collectors.toList())

        specializationResponse.syllabusResponse == (specializationEntity.getSyllabus() != null
                ? SYLLABUS_MAPPER.buildSyllabusResponse(specializationEntity.getSyllabus())
                : null)

    }


    // updateSpecialization
    def "TestUpdateSpecialization"() {
        given:
        def specializationEntity = random.nextObject(SpecializationEntity)
        def updateSpecializationRequest = random.nextObject(UpdateSpecializationRequest)

        when:
        SPECIALIZATION_MAPPER.updateSpecialization(specializationEntity, updateSpecializationRequest)

        then:
        updateSpecializationRequest.description == specializationEntity.description
        IN_PROGRESS == specializationEntity.status
    }

}
