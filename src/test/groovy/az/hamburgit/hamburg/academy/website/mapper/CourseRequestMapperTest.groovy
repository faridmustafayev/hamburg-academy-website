package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.CourseRequestEntity
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateCourseRequestInput
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateCourseRequestInput
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import java.util.stream.Collectors

import static az.hamburg.it.hamburg.academy.website.mapper.CourseRequestMapper.COURSE_REQUEST_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class CourseRequestMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // buildCourseRequestEntity
    def "TestBuildCourseRequestEntity"() {
        given:
        def courseRequestInput = random.nextObject(CreateCourseRequestInput)
        def specializations = random.nextObject(List<SpecializationEntity>)

        when:
        def courseRequestEntity = COURSE_REQUEST_MAPPER.buildCourseRequestEntity(courseRequestInput, specializations)

        then:
        courseRequestInput.phoneNumber == courseRequestEntity.phoneNumber
        courseRequestInput.fullName == courseRequestEntity.fullName
        specializations == courseRequestEntity.specializations
        ACTIVE == courseRequestEntity.status
    }

    // buildCourseRequestOutput
    def "TestBuildCourseRequestOutput"() {
        given:
        def courseRequestEntity = random.nextObject(CourseRequestEntity)

        when:
        def courseRequestResponse = COURSE_REQUEST_MAPPER.buildCourseRequestOutput(courseRequestEntity)

        then:
        courseRequestEntity.id == courseRequestResponse.id
        courseRequestEntity.fullName == courseRequestResponse.fullName
        courseRequestEntity.phoneNumber == courseRequestResponse.phoneNumber
        courseRequestEntity.status == courseRequestResponse.status
        courseRequestEntity.createdAt == courseRequestResponse.createdAt
        courseRequestEntity.updatedAt == courseRequestResponse.updatedAt
        courseRequestEntity.specializations.stream()
                .map(SpecializationEntity::getId)
                .collect(Collectors.toList()) == courseRequestResponse.specializationIds
    }

    // updateCourseRequest
    def "TestUpdateCourseRequest"() {
        given:
        def courseRequestEntity = random.nextObject(CourseRequestEntity)
        def courseRequestInput = random.nextObject(UpdateCourseRequestInput)

        when:
        COURSE_REQUEST_MAPPER.updateCourseRequest(courseRequestEntity, courseRequestInput)

        then:
        courseRequestEntity.fullName == courseRequestInput.fullName
        courseRequestEntity.phoneNumber == courseRequestInput.phoneNumber
        courseRequestEntity.status == IN_PROGRESS
    }

}
