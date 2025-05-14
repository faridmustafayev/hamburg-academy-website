package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.DiplomaEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.DiplomaRepository;
import az.hamburg.it.hamburg.academy.website.dao.repository.GraduateRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateDiplomaRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateDiplomaRequest;
import az.hamburg.it.hamburg.academy.website.model.response.DiplomaResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.DiplomaService;
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.DIPLOMA_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.GRADUATE_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.DiplomaMapper.DIPLOMA_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.DELETED;
import static az.hamburg.it.hamburg.academy.website.util.FileUtils.extractFileNameFromUrl;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@FieldDefaults(level = PRIVATE)
@RequiredArgsConstructor
public class DiplomaServiceHandler implements DiplomaService {
    final DiplomaRepository diplomaRepository;
    final GraduateRepository graduateRepository;
    final MinioService minioService;
    @Value("${minio.buckets.diplomas.name}")
    String diplomaBucket;

    @Override
    public void saveDiploma(CreateDiplomaRequest request) {
        var graduateEntity = graduateRepository.findById(request.getGraduateId()).orElseThrow(() ->
                new NotFoundException(GRADUATE_NOT_FOUND.getCode(), GRADUATE_NOT_FOUND.getMessage(request.getGraduateId())));

        var diplomaEntity = DIPLOMA_MAPPER.buildDiplomaEntity(request, graduateEntity);

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            var imageFileName = minioService.uploadFile(request.getImageFile(), diplomaBucket);
            var imageUrl = minioService.getFileUrl(imageFileName, diplomaBucket);
            diplomaEntity.setDiplomaPath(imageUrl);
        }

        diplomaRepository.save(diplomaEntity);
    }

    @Override
    public void deleteDiploma(Long id) {
        var diplomaEntity = fetchDiplomaIfExist(id);
        diplomaEntity.setStatus(DELETED);
        diplomaRepository.save(diplomaEntity);
    }

    @Override
    public void updateDiploma(Long id, UpdateDiplomaRequest request) {
        var diplomaEntity = fetchDiplomaIfExist(id);

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            if (diplomaEntity.getDiplomaPath() != null) {
                var fileName = extractFileNameFromUrl(diplomaEntity.getDiplomaPath());
                minioService.deleteFile(fileName, diplomaBucket);
            }
            var uploadFile = minioService.uploadFile(request.getImageFile(), diplomaBucket);
            var fileUrl = minioService.getFileUrl(uploadFile, diplomaBucket);
            diplomaEntity.setDiplomaPath(fileUrl);
        }

        DIPLOMA_MAPPER.updateDiploma(diplomaEntity, request);
        diplomaRepository.save(diplomaEntity);
    }

    @Override
    public DiplomaResponse getDiploma(Long id) {
        var diplomaEntity = fetchDiplomaIfExist(id);
        return DIPLOMA_MAPPER.buildDiplomaResponse(diplomaEntity);
    }

    @Override
    public List<DiplomaResponse> getDiplomas() {
        return diplomaRepository.findAll().stream()
                .map(DIPLOMA_MAPPER::buildDiplomaResponse)
                .collect(Collectors.toList());
    }

    private DiplomaEntity fetchDiplomaIfExist(Long id) {
        return diplomaRepository.findById(id).orElseThrow(() ->
                new NotFoundException(DIPLOMA_NOT_FOUND.getCode(), DIPLOMA_NOT_FOUND.getMessage(id)));
    }

}
