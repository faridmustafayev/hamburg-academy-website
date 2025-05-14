package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.ProjectRequestEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.ServiceEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.ServiceRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateServiceRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateServiceRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ServiceResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.ServiceAbstraction;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.SERVICE_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.ServiceMapper.SERVICE_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.DELETED;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ServiceImpl implements ServiceAbstraction {
    ServiceRepository serviceRepository;

    @Override
    public void saveService(CreateServiceRequest request) {
        var serviceEntity = SERVICE_MAPPER.buildServiceEntity(request);
        serviceRepository.save(serviceEntity);
    }

    @Override
    public ServiceResponse getService(Long id) {
        var serviceEntity = fetchServiceIfExist(id);
        return SERVICE_MAPPER.buildServiceResponse(serviceEntity);
    }

    @Override
    public List<ServiceResponse> getServices() {
        return serviceRepository.findAll().stream()
                .map(SERVICE_MAPPER::buildServiceResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateService(Long id, UpdateServiceRequest request) {
        var serviceEntity = fetchServiceIfExist(id);
        SERVICE_MAPPER.updateService(serviceEntity, request);
        serviceRepository.save(serviceEntity);
    }

    @Override
    public void deleteService(Long id) {
        var serviceEntity = fetchServiceIfExist(id);

        if (!serviceEntity.getProjectRequests().isEmpty()) {
            for (ProjectRequestEntity projectRequest : serviceEntity.getProjectRequests()) {
                projectRequest.setStatus(DELETED);
            }
        }

        serviceEntity.setStatus(DELETED);
        serviceRepository.save(serviceEntity);
    }

    private ServiceEntity fetchServiceIfExist(Long id) {
        return serviceRepository.findById(id).orElseThrow(() ->
                new NotFoundException(SERVICE_NOT_FOUND.getCode(), SERVICE_NOT_FOUND.getMessage(id)));
    }

}
