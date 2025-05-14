package az.hamburg.it.hamburg.academy.website.annotation;

import az.hamburg.it.hamburg.academy.website.validation.PdfFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PdfFileValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValidPdf {
    String message() default "Only PDF files are allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
