package az.hamburg.it.hamburg.academy.website.annotation;

import az.hamburg.it.hamburg.academy.website.validation.ImageFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ImageFileValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ValidImage {
    String message() default "Only JPG, JPEG, PNG, WEBP image formats are allowed (max 5MB)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
