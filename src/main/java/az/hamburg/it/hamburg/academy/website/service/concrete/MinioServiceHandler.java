package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.exception.FileStorageException;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.service.abstraction.MinioService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.FILE_NOT_DELETED;
import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.FILE_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.FILE_NOT_UPLOADED;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MinioServiceHandler implements MinioService {
    MinioClient minioClient;

    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        var originalFileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown";
        var fileName = UUID.randomUUID() + "_" + originalFileName;
        log.info("uploadFile started | bucketName: {}, originalFileName: {}", bucketName, originalFileName);

        try {
            var fileStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(fileStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.info("uploadFile success | fileName: {}", fileName);
            return fileName;
        } catch (Exception e) {
            log.error("uploadFile error | bucketName: {}, originalFileName: {}, error: {}", bucketName, originalFileName, e.getMessage(), e);
            throw new FileStorageException(FILE_NOT_UPLOADED.getCode(), FILE_NOT_UPLOADED.getMessage());
        }
    }

    @Override
    public String getFileUrl(String fileName, String bucketName) {
        log.info("getFileUrl started | bucketName: {}, fileName: {}", bucketName, fileName);
        try {
            var url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .method(Method.GET)
                            .build()
            );
            log.info("getFileUrl success | fileUrl: {}", url);
            return url;
        } catch (Exception e) {
            log.error("getFileUrl error | bucketName: {}, fileName: {}, error: {}", bucketName, fileName, e.getMessage(), e);
            throw new NotFoundException(FILE_NOT_FOUND.getCode(), FILE_NOT_FOUND.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileName, String bucketName) {
        log.info("deleteFile started | bucketName: {}, fileName: {}", bucketName, fileName);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            log.info("deleteFile success | fileName: {}", fileName);
        } catch (Exception e) {
            log.error("deleteFile error | bucketName: {}, fileName: {}, error: {}", bucketName, fileName, e.getMessage(), e);
            throw new FileStorageException(FILE_NOT_DELETED.getCode(), FILE_NOT_DELETED.getMessage());
        }
    }

}
