package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.ProjectRequestEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.ServiceEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.ProjectRequestRepository;
import az.hamburg.it.hamburg.academy.website.dao.repository.ServiceRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateProjectRequestInput;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateProjectRequestInput;
import az.hamburg.it.hamburg.academy.website.model.response.ProjectRequestOutput;
import az.hamburg.it.hamburg.academy.website.service.abstraction.ProjectRequestService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.PROJECT_REQUEST_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.SERVICE_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.ProjectRequestMapper.PROJECT_REQUEST_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.DELETED;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProjectRequestServiceHandler implements ProjectRequestService {
    ProjectRequestRepository projectRequestRepository;
    ServiceRepository serviceRepository;
    EntityManager entityManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveProjectRequest(CreateProjectRequestInput requestInput) {
        var serviceIds = requestInput.getServiceIds();
        var allById = serviceRepository.findAllById(serviceIds);

        if (allById.size() != serviceIds.size()) {
            throw new NotFoundException(SERVICE_NOT_FOUND.getCode(), SERVICE_NOT_FOUND.getMessage(serviceIds));
        }

        var projectRequestEntity = PROJECT_REQUEST_MAPPER.buildProjectRequestEntity(requestInput, allById);
        projectRequestRepository.save(projectRequestEntity);

        for (ServiceEntity service : allById) {
            String sql = "INSERT INTO service_project_requests (service_id, project_request_id) VALUES (:serviceId, :projectRequestId)";
            entityManager.createNativeQuery(sql)
                    .setParameter("serviceId", service.getId())
                    .setParameter("projectRequestId", projectRequestEntity.getId())
                    .executeUpdate();
        }
    }

    @Override
    public ProjectRequestOutput getProjectRequest(Long id) {
        var projectRequestEntity = fetchProjectRequestIfExist(id);
        return PROJECT_REQUEST_MAPPER.buildProjectRequestOutput(projectRequestEntity);
    }

    @Override
    public List<ProjectRequestOutput> getProjectRequests() {
        return projectRequestRepository.findAll().stream()
                .map(PROJECT_REQUEST_MAPPER::buildProjectRequestOutput)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateProjectRequest(Long id, UpdateProjectRequestInput requestInput) {
        var projectRequestEntity = fetchProjectRequestIfExist(id);

        if (requestInput.getServiceIds() != null) {

            var allById = serviceRepository.findAllById(requestInput.getServiceIds());

            String deleteSql = """
                            DELETE FROM service_project_requests
                            WHERE service_id = :serviceId
                            AND project_request_id NOT IN (:ids)
                    """;

            entityManager.createNativeQuery(deleteSql)
                    .setParameter("projectRequestId", projectRequestEntity.getId())
                    .setParameter("ids", requestInput.getServiceIds())
                    .executeUpdate();

            for (ServiceEntity service : allById) {
                String insertSql = """
                                    INSERT INTO service_project_requests (service_id, project_request_id)
                                    SELECT :serviceId, :projectRequestId
                                    WHERE NOT EXISTS (
                                        SELECT 1 FROM service_project_requests
                                        WHERE service_id = :serviceId
                                        AND project_request_id = :projectRequestId
                                    )
                        """;

                entityManager.createNativeQuery(insertSql)
                        .setParameter("serviceId", service.getId())
                        .setParameter("projectRequestId", projectRequestEntity.getId())
                        .executeUpdate();
            }
        }

        PROJECT_REQUEST_MAPPER.updateProjectRequest(projectRequestEntity, requestInput);
        projectRequestRepository.save(projectRequestEntity);
    }

    @Override
    public void deleteProjectRequest(Long id) {
        var projectRequestEntity = fetchProjectRequestIfExist(id);
        projectRequestEntity.setStatus(DELETED);
        projectRequestRepository.save(projectRequestEntity);
    }

    private ProjectRequestEntity fetchProjectRequestIfExist(Long id) {
        return projectRequestRepository.findById(id).orElseThrow(() ->
                new NotFoundException(PROJECT_REQUEST_NOT_FOUND.getCode(), PROJECT_REQUEST_NOT_FOUND.getMessage(id)));
    }

}
