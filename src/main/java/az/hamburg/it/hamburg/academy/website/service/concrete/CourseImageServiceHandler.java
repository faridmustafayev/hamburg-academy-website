package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.dao.entity.CourseImageEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.CourseImageRepository;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.CreateOrUpdateCourseImageRequest;
import az.hamburg.it.hamburg.academy.website.model.response.CourseImageResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.CourseImageService;
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.COURSE_IMAGE_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.CourseImageMapper.COURSE_IMAGE_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.DELETED;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;
import static az.hamburg.it.hamburg.academy.website.util.FileUtils.extractFileNameFromUrl;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CourseImageServiceHandler implements CourseImageService {
    final CourseImageRepository courseImageRepository;
    final MinioService minioService;
    @Value("${minio.buckets.course-images.name}")
    String courseImageBucket;

    @Override
    public void saveCourseImage(CreateOrUpdateCourseImageRequest request) {
        var courseImageEntity = COURSE_IMAGE_MAPPER.buildCourseImageEntity(request);

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            var uploadFile = minioService.uploadFile(request.getImageFile(), courseImageBucket);
            var fileUrl = minioService.getFileUrl(uploadFile, courseImageBucket);
            courseImageEntity.setImagePath(fileUrl);
        }

        courseImageRepository.save(courseImageEntity);
    }

    @Override
    public CourseImageResponse getCourseImage(Long id) {
        var courseImageEntity = fetchCourseImageIfExist(id);
        return COURSE_IMAGE_MAPPER.buildCourseImageResponse(courseImageEntity);
    }

    @Override
    public List<CourseImageResponse> getCourseImages() {
        return courseImageRepository.findAll().stream()
                .map(COURSE_IMAGE_MAPPER::buildCourseImageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateCourseImage(Long id, CreateOrUpdateCourseImageRequest request) {
        var courseImageEntity = fetchCourseImageIfExist(id);

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            if (courseImageEntity.getImagePath() != null) {
                String objectName = extractFileNameFromUrl(courseImageEntity.getImagePath());
                minioService.deleteFile(objectName, courseImageBucket);
            }
            var uploadFile = minioService.uploadFile(request.getImageFile(), courseImageBucket);
            var fileUrl = minioService.getFileUrl(uploadFile, courseImageBucket);
            courseImageEntity.setImagePath(fileUrl);
        }

        courseImageEntity.setStatus(IN_PROGRESS);
        courseImageRepository.save(courseImageEntity);
    }

    @Override
    public void deleteCourseImage(Long id) {
        var courseImageEntity = fetchCourseImageIfExist(id);
        courseImageEntity.setStatus(DELETED);
        courseImageRepository.save(courseImageEntity);
    }

    private CourseImageEntity fetchCourseImageIfExist(Long id) {
        return courseImageRepository.findById(id).orElseThrow(() ->
                new NotFoundException(COURSE_IMAGE_NOT_FOUND.getCode(), COURSE_IMAGE_NOT_FOUND.getMessage(id)));
    }

}
