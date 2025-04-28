package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.ProjectRequestEntity
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateProjectRequestInput
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateProjectRequestInput
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.ProjectRequestMapper.PROJECT_REQUEST_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class ProjectRequestMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // buildProjectRequestEntity
    def "TestBuildProjectRequestEntity"() {
        given:
        def projectRequestInput = random.nextObject(CreateProjectRequestInput)

        when:
        def projectRequestEntity = PROJECT_REQUEST_MAPPER.buildProjectRequestEntity(projectRequestInput)

        then:
        projectRequestInput.fullName == projectRequestEntity.fullName
        projectRequestInput.email == projectRequestEntity.email
        projectRequestInput.phoneNumber == projectRequestEntity.phoneNumber
        ACTIVE == projectRequestEntity.status
    }

    // buildProjectRequestOutput
    def "TestBuildProjectRequestOutput"() {
        given:
        def projectRequestEntity = random.nextObject(ProjectRequestEntity)

        when:
        def projectRequestOutput = PROJECT_REQUEST_MAPPER.buildProjectRequestOutput(projectRequestEntity)

        then:
        projectRequestEntity.id == projectRequestOutput.id
        projectRequestEntity.fullName == projectRequestOutput.fullName
        projectRequestEntity.email == projectRequestOutput.email
        projectRequestEntity.phoneNumber == projectRequestOutput.phoneNumber
        projectRequestEntity.status == projectRequestOutput.status
        projectRequestEntity.createdAt == projectRequestOutput.createdAt
        projectRequestEntity.updatedAt == projectRequestOutput.updatedAt
    }

    // updateProjectRequest
    def "TestUpdateProjectRequest"() {
        given:
        def projectRequestEntity = random.nextObject(ProjectRequestEntity)
        def updateProjectRequestInput = random.nextObject(UpdateProjectRequestInput)

        when:
        PROJECT_REQUEST_MAPPER.updateProjectRequest(projectRequestEntity, updateProjectRequestInput)

        then:
        projectRequestEntity.fullName == updateProjectRequestInput.fullName
        projectRequestEntity.email == updateProjectRequestInput.email
        projectRequestEntity.phoneNumber == updateProjectRequestInput.phoneNumber
        projectRequestEntity.status == IN_PROGRESS
    }

}
