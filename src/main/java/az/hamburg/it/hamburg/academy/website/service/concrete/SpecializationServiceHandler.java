package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.CourseRequestEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.InstructorEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.SyllabusEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.SpecializationRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateSpecializationRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateSpecializationRequest;
import az.hamburg.it.hamburg.academy.website.model.response.SpecializationResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService;
import az.hamburg.it.hamburg.academy.website.service.abstraction.SpecializationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.SPECIALIZATION_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.SpecializationMapper.SPECIALIZATION_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.DELETED;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@FieldDefaults(level = PRIVATE)
@RequiredArgsConstructor
public class SpecializationServiceHandler implements SpecializationService {
    private final SpecializationRepository specializationRepository;
    private final MinioService minioService;
    @Value("${minio.buckets.syllabuses.name}")
    String syllabusBucket;

    @Override
    public void saveSpecialization(CreateSpecializationRequest request) {
        var specialization = SpecializationEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(ACTIVE)
                .build();

        String uploadImageFile = minioService.uploadFile(request.getImageFile(), syllabusBucket);
        var imageFileUrl = minioService.getFileUrl(uploadImageFile, syllabusBucket);

        String uploadPdfFile = minioService.uploadFile(request.getPdfFile(), syllabusBucket);
        var pdfFileUrl = minioService.getFileUrl(uploadPdfFile, syllabusBucket);

        var syllabus = SyllabusEntity.builder()
                .specialization(specialization)
                .imagePath(imageFileUrl)
                .pdfPath(pdfFileUrl)
                .status(ACTIVE)
                .build();

        specialization.setSyllabus(syllabus);
        specializationRepository.save(specialization);
    }

    @Override
    public SpecializationResponse getSpecialization(Long id) {
        var specializationEntity = fetchSpecializationIfExist(id);
        return SPECIALIZATION_MAPPER.buildSpecializationResponse(specializationEntity);
    }

    @Override
    public List<SpecializationResponse> getAll() {
        var specializationEntities = specializationRepository.findAll();
        return specializationEntities.stream()
                .map(SPECIALIZATION_MAPPER::buildSpecializationResponse)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteSpecialization(Long id) {
        var specialization = fetchSpecializationIfExist(id);

        if (!specialization.getCourseRequests().isEmpty()) {
            for (CourseRequestEntity courseRequest : specialization.getCourseRequests()) {
                courseRequest.setStatus(DELETED);
            }
        }

        if (!specialization.getInstructors().isEmpty()) {
            for (InstructorEntity instructor : specialization.getInstructors()) {
                instructor.setStatus(DELETED);
            }
        }

        if (specialization.getSyllabus() != null) {
            specialization.getSyllabus().setStatus(DELETED);
        }

        specialization.setStatus(DELETED);
        specializationRepository.save(specialization);
    }

    @Override
    public void updateSpecialization(Long id, UpdateSpecializationRequest request) {
        var specialization = fetchSpecializationIfExist(id);
        SPECIALIZATION_MAPPER.updateSpecialization(specialization, request);
        specializationRepository.save(specialization);
    }

    @Override
    public SpecializationEntity fetchSpecializationIfExist(Long id) {
        return specializationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(SPECIALIZATION_NOT_FOUND.getCode(), SPECIALIZATION_NOT_FOUND.getMessage(id)));
    }
}
