package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.CourseRequestEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.CourseRequestRepository;
import az.hamburg.it.hamburg.academy.website.dao.repository.SpecializationRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateCourseRequestInput;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateCourseRequestInput;
import az.hamburg.it.hamburg.academy.website.model.response.CourseRequestOutput;
import az.hamburg.it.hamburg.academy.website.service.abstraction.CourseRequestService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.COURSE_REQUEST_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.SPECIALIZATION_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.CourseRequestMapper.COURSE_REQUEST_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.DELETED;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CourseRequestServiceHandler implements CourseRequestService {
    CourseRequestRepository courseRequestRepository;
    SpecializationRepository specializationRepository;
    EntityManager entityManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveCourseRequest(CreateCourseRequestInput requestInput) {
        var specializationIds = requestInput.getSpecializationIds();
        var allIds = specializationRepository.findAllById(specializationIds);

        if (allIds.size() != specializationIds.size()) {
            throw new NotFoundException(SPECIALIZATION_NOT_FOUND.getCode(), SPECIALIZATION_NOT_FOUND.getMessage(specializationIds));
        }

        var courseRequestEntity = COURSE_REQUEST_MAPPER.buildCourseRequestEntity(requestInput, allIds);
        courseRequestRepository.save(courseRequestEntity);

        for (SpecializationEntity specialization : allIds) {
            String sql = "INSERT INTO specialization_course_requests (specialization_id, course_request_id) VALUES (:specializationId, :courseRequestId)";
            entityManager.createNativeQuery(sql)
                    .setParameter("specializationId", specialization.getId())
                    .setParameter("courseRequestId", courseRequestEntity.getId())
                    .executeUpdate();
        }
    }

    @Override
    public CourseRequestOutput getCourseRequest(Long id) {
        var courseRequestEntity = fetchCourseRequestIfExist(id);
        return COURSE_REQUEST_MAPPER.buildCourseRequestOutput(courseRequestEntity);
    }

    @Override
    public List<CourseRequestOutput> getCourseRequests() {
        return courseRequestRepository.findAll().stream()
                .map(COURSE_REQUEST_MAPPER::buildCourseRequestOutput)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourseRequests(Long id) {
        var courseRequestEntity = fetchCourseRequestIfExist(id);
        courseRequestEntity.setStatus(DELETED);
        courseRequestRepository.save(courseRequestEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCourseRequests(Long id, UpdateCourseRequestInput requestInput) {
        var courseRequestEntity = fetchCourseRequestIfExist(id);

        if (requestInput.getSpecializationIds() != null) {

            var allById = specializationRepository.findAllById(requestInput.getSpecializationIds());

            String deleteSql = """
                            DELETE FROM specialization_course_requests
                            WHERE course_request_id = :courseRequestId
                            AND specialization_id NOT IN (:ids)
                    """;

            entityManager.createNativeQuery(deleteSql)
                    .setParameter("courseRequestId", courseRequestEntity.getId())
                    .setParameter("ids", requestInput.getSpecializationIds())
                    .executeUpdate();

            for (SpecializationEntity specialization : allById) {
                String insertSql = """
                                    INSERT INTO specialization_course_requests (specialization_id, course_request_id)
                                    SELECT :specializationId, :courseRequestId
                                    WHERE NOT EXISTS (
                                        SELECT 1 FROM specialization_course_requests
                                        WHERE specialization_id = :specializationId
                                        AND course_request_id = :courseRequestId
                                    )
                        """;

                entityManager.createNativeQuery(insertSql)
                        .setParameter("specializationId", specialization.getId())
                        .setParameter("courseRequestId", courseRequestEntity.getId())
                        .executeUpdate();
            }
        }

        COURSE_REQUEST_MAPPER.updateCourseRequest(courseRequestEntity, requestInput);
        courseRequestRepository.save(courseRequestEntity);
    }

    private CourseRequestEntity fetchCourseRequestIfExist(Long id) {
        return courseRequestRepository.findById(id).orElseThrow(() ->
                new NotFoundException(COURSE_REQUEST_NOT_FOUND.getCode(), COURSE_REQUEST_NOT_FOUND.getMessage(id)));
    }

}
