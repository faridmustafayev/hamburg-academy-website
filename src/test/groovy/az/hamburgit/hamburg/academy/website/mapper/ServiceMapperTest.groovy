package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.ServiceEntity
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateServiceRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateServiceRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.ServiceMapper.SERVICE_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class ServiceMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // buildServiceEntity
    def "TestBuildServiceEntity"() {
        given:
        def createServiceRequest = random.nextObject(CreateServiceRequest)

        when:
        def serviceEntity = SERVICE_MAPPER.buildServiceEntity(createServiceRequest)

        then:
        createServiceRequest.name == serviceEntity.name
        createServiceRequest.description == serviceEntity.description
        ACTIVE == serviceEntity.status
    }

    // buildServiceResponse
    def "TestBuildServiceResponse"() {
        given:
        def serviceEntity = random.nextObject(ServiceEntity)

        when:
        def serviceResponse = SERVICE_MAPPER.buildServiceResponse(serviceEntity)

        then:
        serviceResponse.id == serviceEntity.id
        serviceResponse.name == serviceEntity.name
        serviceResponse.description == serviceEntity.description
        serviceResponse.status == serviceEntity.status
        serviceResponse.createdAt == serviceEntity.createdAt
        serviceResponse.updatedAt == serviceEntity.updatedAt
    }

    // updateService
    def "TestUpdateService"() {
        given:
        def serviceEntity = random.nextObject(ServiceEntity)
        def updateServiceRequest = random.nextObject(UpdateServiceRequest)

        when:
        SERVICE_MAPPER.updateService(serviceEntity, updateServiceRequest)

        then:
        updateServiceRequest.name == serviceEntity.name
        updateServiceRequest.description == serviceEntity.description
        IN_PROGRESS == serviceEntity.status
    }
}
