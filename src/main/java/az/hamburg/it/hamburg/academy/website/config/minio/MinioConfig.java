package az.hamburg.it.hamburg.academy.website.config.minio;

import az.hamburg.it.hamburg.academy.website.exception.MinioInitializationException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.MINIO_INITIALIZATION_EXCEPTION;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MinioConfig {
    MinioProperties properties;
    MinioClient minioClient;

    @PostConstruct
    public void initializeBuckets() {
        try {
            List<String> bucketNames = List.of(
                    properties.getBuckets().getSyllabuses().getName(),
                    properties.getBuckets().getInstructors().getName(),
                    properties.getBuckets().getCourseImages().getName(),
                    properties.getBuckets().getGraduates().getName(),
                    properties.getBuckets().getReviews().getName(),
                    properties.getBuckets().getDiplomas().getName()
            );

            for (String bucketName : bucketNames) {
                boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!exists) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                    log.info("✅ Created bucket: " + bucketName);
                } else {
                    log.info("ℹ️Bucket already exists: " + bucketName);
                }
            }
        } catch (Exception e) {
            throw new MinioInitializationException(MINIO_INITIALIZATION_EXCEPTION.getCode(), MINIO_INITIALIZATION_EXCEPTION.getMessage());
        }
    }
}
