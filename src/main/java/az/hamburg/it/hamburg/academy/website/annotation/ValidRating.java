package az.hamburg.it.hamburg.academy.website.annotation;

import az.hamburg.it.hamburg.academy.website.validation.RatingValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = RatingValidator.class)
public @interface ValidRating {
    String message() default "Rating must be between 1.0 and 5.0 in 0.5 steps (e.g. 1.0, 1.5, ..., 5.0)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
