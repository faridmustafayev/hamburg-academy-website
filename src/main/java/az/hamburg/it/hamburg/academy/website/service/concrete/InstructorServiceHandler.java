package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.InstructorEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.InstructorRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateInstructorRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateInstructorRequest;
import az.hamburg.it.hamburg.academy.website.model.response.InstructorResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.InstructorService;
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService;
import az.hamburg.it.hamburg.academy.website.service.abstraction.SpecializationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.INSTRUCTOR_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.InstructorMapper.INSTRUCTOR_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.DELETED;
import static az.hamburg.it.hamburg.academy.website.util.FileUtils.extractFileNameFromUrl;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class InstructorServiceHandler implements InstructorService {
    final InstructorRepository instructorRepository;
    final SpecializationService specializationServices;
    final MinioService minioService;
    @Value("${minio.buckets.instructors.name}")
    String instructorBucket;

    @Override
    public void saveInstructor(CreateInstructorRequest request) {
        var specializationEntity = specializationServices.fetchSpecializationIfExist(request.getSpecializationId());
        var instructor = INSTRUCTOR_MAPPER.buildInstructorEntity(request, specializationEntity);

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            var imageFileName = minioService.uploadFile(request.getImageFile(), instructorBucket);
            var imageUrl = minioService.getFileUrl(imageFileName, instructorBucket);
            instructor.setImagePath(imageUrl);
        }

        instructorRepository.save(instructor);
    }

    @Override
    public InstructorResponse getInstructor(Long id) {
        var instructor = fetchInstructorIfExist(id);
        return INSTRUCTOR_MAPPER.buildInstructorResponse(instructor);
    }

    @Override
    public List<InstructorResponse> getAll() {
        var instructorEntities = instructorRepository.findAll();
        return instructorEntities.stream()
                .map(INSTRUCTOR_MAPPER::buildInstructorResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteInstructor(Long id) {
        var instructor = fetchInstructorIfExist(id);
        instructor.setStatus(DELETED);
        instructorRepository.save(instructor);
    }

    @Override
    public void updateInstructor(Long id, UpdateInstructorRequest instructorRequest) {
        var instructor = fetchInstructorIfExist(id);

        if (instructorRequest.getImageFile() != null && !instructorRequest.getImageFile().isEmpty()) {
            if (instructor.getImagePath() != null) {
                var fileName = extractFileNameFromUrl(instructor.getImagePath());
                minioService.deleteFile(fileName, instructorBucket);
            }
            var uploadFile = minioService.uploadFile(instructorRequest.getImageFile(), instructorBucket);
            var fileUrl = minioService.getFileUrl(uploadFile, instructorBucket);
            instructor.setImagePath(fileUrl);
        }

        INSTRUCTOR_MAPPER.updateInstructor(instructor, instructorRequest);
        instructorRepository.save(instructor);
    }

    private InstructorEntity fetchInstructorIfExist(Long id) {
        return instructorRepository.findById(id).orElseThrow(() ->
                new NotFoundException(INSTRUCTOR_NOT_FOUND.getCode(), INSTRUCTOR_NOT_FOUND.getMessage(id)));
    }

}