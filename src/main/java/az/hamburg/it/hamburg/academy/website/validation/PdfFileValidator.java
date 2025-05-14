package az.hamburg.it.hamburg.academy.website.validation;

import az.hamburg.it.hamburg.academy.website.annotation.ValidPdf;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class PdfFileValidator implements ConstraintValidator<ValidPdf, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && contentType.equals("application/pdf");
    }
}
