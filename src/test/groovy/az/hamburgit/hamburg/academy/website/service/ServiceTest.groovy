package az.hamburgit.hamburg.academy.website.service

import az.hamburg.it.hamburg.academy.website.dao.entity.ServiceEntity
import az.hamburg.it.hamburg.academy.website.dao.repository.ServiceRepository
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateServiceRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateServiceRequest
import az.hamburg.it.hamburg.academy.website.service.abstraction.ServiceAbstraction
import az.hamburg.it.hamburg.academy.website.service.concrete.ServiceImpl
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.ServiceMapper.SERVICE_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class ServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    ServiceAbstraction serviceAbstraction
    ServiceRepository serviceRepository

    def setup() {
        serviceRepository = Mock()
        serviceAbstraction = new ServiceImpl(serviceRepository)
    }

    // saveService
    def "TestSaveService"() {
        given:
        def createServiceRequest = random.nextObject(CreateServiceRequest)
        def serviceEntity = SERVICE_MAPPER.buildServiceEntity(createServiceRequest)

        when:
        serviceAbstraction.saveService(createServiceRequest)

        then:
        1 * serviceRepository.save(serviceEntity)
    }

    // getService
    def "TestGetService success case"() {
        given:
        def id = random.nextObject(Long)
        def serviceEntity = random.nextObject(ServiceEntity)

        when:
        def serviceResponse = serviceAbstraction.getService(id)

        then:
        1 * serviceRepository.findById(id) >> Optional.of(serviceEntity)
        serviceResponse.status == serviceEntity.status
        serviceResponse.id == serviceEntity.id
        serviceResponse.name == serviceEntity.name
        serviceResponse.description == serviceEntity.description
        serviceResponse.createdAt == serviceEntity.createdAt
        serviceResponse.updatedAt == serviceEntity.updatedAt
    }

    def "TestGetService ServiceNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        serviceAbstraction.getService(id)

        then:
        1 * serviceRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "SERVICE_NOT_FOUND"
        ex.getMessage() == "No service with id (ID: %s) was found".formatted(id)
    }

    // getServices
    def "TestGetServices"() {
        given:
        def serviceEntity = random.nextObject(ServiceEntity)
        def services = [serviceEntity]

        when:
        def serviceResponses = serviceAbstraction.getServices()
        def serviceResponse = SERVICE_MAPPER.buildServiceResponse(serviceEntity)

        then:
        1 * serviceRepository.findAll() >> services
        serviceResponse.id == serviceEntity.id
        serviceResponse.name == serviceEntity.name
        serviceResponse.description == serviceEntity.description
        serviceResponse.status == serviceEntity.status
        serviceResponse.createdAt == serviceEntity.createdAt
        serviceResponse.updatedAt == serviceEntity.updatedAt
        serviceResponses == [serviceResponse]
    }

    // updateService
    def "TestUpdateService success case"() {
        given:
        def id = random.nextObject(Long)
        def updateServiceRequest = random.nextObject(UpdateServiceRequest)
        def serviceEntity = random.nextObject(ServiceEntity)

        when:
        serviceAbstraction.updateService(id, updateServiceRequest)

        then:
        1 * serviceRepository.findById(id) >> Optional.of(serviceEntity)
        IN_PROGRESS == serviceEntity.status
        updateServiceRequest.name == serviceEntity.name
        updateServiceRequest.description == serviceEntity.description
        1 * serviceRepository.save(serviceEntity)
    }

    def "TestUpdateService ServiceNotFound case"() {
        given:
        def id = random.nextObject(Long)
        def updateServiceRequest = random.nextObject(UpdateServiceRequest)

        when:
        serviceAbstraction.updateService(id, updateServiceRequest)

        then:
        1 * serviceRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "SERVICE_NOT_FOUND"
        ex.getMessage() == "No service with id (ID: %s) was found".formatted(id)
        0 * serviceRepository.save(_)
    }

    // deleteService
    def "TestDeleteService success case"() {
        given:
        def id = random.nextObject(Long)
        def serviceEntity = random.nextObject(ServiceEntity)

        when:
        serviceAbstraction.deleteService(id)

        then:
        1 * serviceRepository.findById(id) >> Optional.of(serviceEntity)
        1 * serviceRepository.deleteByIdCustom(serviceEntity.id)
    }

    def "TestDeleteService ServiceNotFound case"() {
        given:
        def id = random.nextObject(Long)

        when:
        serviceAbstraction.deleteService(id)

        then:
        1 * serviceRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.getCode() == "SERVICE_NOT_FOUND"
        ex.getMessage() == "No service with id (ID: %s) was found".formatted(id)
        0 * serviceRepository.deleteByIdCustom(id)
    }
}
