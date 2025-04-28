package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.GraduateEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.GraduateRepository;
import az.hamburg.it.hamburg.academy.website.dao.repository.SpecializationRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.criteria.GraduateCriteria;
import az.hamburg.it.hamburg.academy.website.model.criteria.PageCriteria;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateGraduateRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateGraduateRequest;
import az.hamburg.it.hamburg.academy.website.model.response.GraduateResponse;
import az.hamburg.it.hamburg.academy.website.model.response.PageableResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.GraduateService;
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService;
import az.hamburg.it.hamburg.academy.website.service.specification.GraduateSpecification;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.GRADUATE_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.SPECIALIZATION_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.GraduateMapper.GRADUATE_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.DELETED;
import static az.hamburg.it.hamburg.academy.website.util.FileUtils.extractFileNameFromUrl;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GraduateServiceHandler implements GraduateService {
    final GraduateRepository graduateRepository;
    final SpecializationRepository specializationRepository;
    final EntityManager entityManager;
    final MinioService minioService;
    @Value("${minio.buckets.graduates.name}")
    String graduateBucket;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveGraduate(CreateGraduateRequest request) {
        var specializationIds = request.getSpecializationIds();
        var allIds = specializationRepository.findAllById(specializationIds);

        if (allIds.size() != specializationIds.size()) {
            throw new NotFoundException(SPECIALIZATION_NOT_FOUND.getCode(), SPECIALIZATION_NOT_FOUND.getMessage(specializationIds));
        }

        var graduateEntity = GRADUATE_MAPPER.buildGraduateEntity(request, allIds);

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            var uploadFile = minioService.uploadFile(request.getImageFile(), graduateBucket);
            var fileUrl = minioService.getFileUrl(uploadFile, graduateBucket);
            graduateEntity.setImagePath(fileUrl);
        }

        graduateRepository.save(graduateEntity);

        for (SpecializationEntity specialization : allIds) {
            String sql = "INSERT INTO specialization_graduates (specialization_id, graduate_id) VALUES (:specializationId, :graduateId)";
            entityManager.createNativeQuery(sql)
                    .setParameter("specializationId", specialization.getId())
                    .setParameter("graduateId", graduateEntity.getId())
                    .executeUpdate();
        }
    }

    @Override
    public GraduateResponse getGraduate(Long id) {
        var graduateEntity = fetchGraduateIfExist(id);
        return GRADUATE_MAPPER.buildGraduateResponse(graduateEntity);
    }

    @Override
    public PageableResponse<GraduateResponse> getGraduates(PageCriteria pageCriteria, GraduateCriteria graduateCriteria) {
        var graduates = graduateRepository.findAll(
                GraduateSpecification.of(graduateCriteria),
                PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount(), Sort.by("id").descending()));

        return GRADUATE_MAPPER.mapPageableGraduateResponse(graduates);
    }

    @Override
    public void deleteGraduate(Long id) {
        var graduateEntity = fetchGraduateIfExist(id);
        graduateEntity.setStatus(DELETED);
        graduateRepository.save(graduateEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateGraduate(Long id, UpdateGraduateRequest request) {
        var graduateEntity = fetchGraduateIfExist(id);

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            if (graduateEntity.getImagePath() != null) {
                var fileName = extractFileNameFromUrl(graduateEntity.getImagePath());
                minioService.deleteFile(fileName, graduateBucket);
            }
            var uploadFile = minioService.uploadFile(request.getImageFile(), graduateBucket);
            var fileUrl = minioService.getFileUrl(uploadFile, graduateBucket);
            graduateEntity.setImagePath(fileUrl);
        }

        var allById = specializationRepository.findAllById(request.getSpecializationIds());

        if (allById != null) {

            String deleteSql = """
                            DELETE FROM specialization_graduates
                            WHERE graduate_id = :graduateId
                            AND specialization_id NOT IN (:ids)
                    """;

            entityManager.createNativeQuery(deleteSql)
                    .setParameter("graduateId", graduateEntity.getId())
                    .setParameter("ids", request.getSpecializationIds())
                    .executeUpdate();

            for (SpecializationEntity specialization : allById) {
                String insertSql = """
                                    INSERT INTO specialization_graduates (specialization_id, graduate_id)
                                    SELECT :specializationId, :graduateId
                                    WHERE NOT EXISTS (
                                        SELECT 1 FROM specialization_graduates
                                        WHERE specialization_id = :specializationId
                                        AND graduate_id = :graduateId
                                    )
                        """;

                entityManager.createNativeQuery(insertSql)
                        .setParameter("specializationId", specialization.getId())
                        .setParameter("graduateId", graduateEntity.getId())
                        .executeUpdate();
            }
        }

        GRADUATE_MAPPER.updateGraduate(graduateEntity, request);
        graduateRepository.save(graduateEntity);
    }

    private GraduateEntity fetchGraduateIfExist(Long id) {
        return graduateRepository.findById(id).orElseThrow(() ->
                new NotFoundException(GRADUATE_NOT_FOUND.getCode(), GRADUATE_NOT_FOUND.getMessage(id)));
    }

}
