package az.hamburg.it.hamburg.academy.website.validation;

import az.hamburg.it.hamburg.academy.website.annotation.ValidImage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        String contentType = file.getContentType();
        if (contentType == null || !isSupportedImageType(contentType)) {
            return false;
        }

        return file.getSize() <= MAX_FILE_SIZE;
    }

    private boolean isSupportedImageType(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/webp") ||
                contentType.equals("image/jpg");
    }
}
