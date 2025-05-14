package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.SyllabusEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.SyllabusRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateSyllabusRequest;
import az.hamburg.it.hamburg.academy.website.model.response.SyllabusResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService;
import az.hamburg.it.hamburg.academy.website.service.abstraction.SyllabusService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.SYLLABUS_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.SyllabusMapper.SYLLABUS_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.DELETED;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;
import static az.hamburg.it.hamburg.academy.website.util.FileUtils.extractFileNameFromUrl;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SyllabusServiceHandler implements SyllabusService {
    final SyllabusRepository syllabusRepository;
    final MinioService minioService;
    @Value("${minio.buckets.syllabuses.name}")
    String syllabusBucket;

    @Override
    public SyllabusResponse getSyllabus(Long id) {
        var syllabusEntity = fetchSyllabusIfExist(id);
        return SYLLABUS_MAPPER.buildSyllabusResponse(syllabusEntity);
    }

    @Override
    public List<SyllabusResponse> getSyllabuses() {
        return syllabusRepository.findAll().stream()
                .map(SYLLABUS_MAPPER::buildSyllabusResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateSyllabus(Long id, UpdateSyllabusRequest syllabusRequest) {
        var syllabusEntity = fetchSyllabusIfExist(id);

        if (syllabusRequest.getImageFile() != null && !syllabusRequest.getImageFile().isEmpty()) {
            if (syllabusEntity.getImagePath() != null) {
                var fileName = extractFileNameFromUrl(syllabusEntity.getImagePath());
                minioService.deleteFile(fileName, syllabusBucket);
            }
            var uploadFile = minioService.uploadFile(syllabusRequest.getImageFile(), syllabusBucket);
            var fileUrl = minioService.getFileUrl(uploadFile, syllabusBucket);
            syllabusEntity.setImagePath(fileUrl);
        }

        if (syllabusRequest.getPdfFile() != null && !syllabusRequest.getPdfFile().isEmpty()) {
            if (syllabusEntity.getPdfPath() != null) {
                var fileName = extractFileNameFromUrl(syllabusEntity.getPdfPath());
                minioService.deleteFile(fileName, syllabusBucket);
            }
            var uploadFile = minioService.uploadFile(syllabusRequest.getPdfFile(), syllabusBucket);
            var fileUrl = minioService.getFileUrl(uploadFile, syllabusBucket);
            syllabusEntity.setPdfPath(fileUrl);
        }

        syllabusEntity.setStatus(IN_PROGRESS);
        syllabusRepository.save(syllabusEntity);
    }

    @Override
    public void deleteSyllabus(Long id) {
        var syllabusEntity = fetchSyllabusIfExist(id);
        syllabusEntity.setStatus(DELETED);
        syllabusRepository.save(syllabusEntity);
    }

    private SyllabusEntity fetchSyllabusIfExist(Long id) {
        return syllabusRepository.findById(id).orElseThrow(() ->
                new NotFoundException(SYLLABUS_NOT_FOUND.getCode(), SYLLABUS_NOT_FOUND.getMessage(id)));
    }

}
