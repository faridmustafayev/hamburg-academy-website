package az.hamburg.it.hamburg.academy.website.service.abstraction;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    String uploadFile(MultipartFile file, String bucketName);

    String getFileUrl(String fileName, String bucketName);

    void deleteFile(String fileName, String bucketName);
}
