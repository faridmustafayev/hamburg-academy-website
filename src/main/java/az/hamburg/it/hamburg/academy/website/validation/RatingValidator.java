package az.hamburg.it.hamburg.academy.website.validation;

import az.hamburg.it.hamburg.academy.website.annotation.ValidRating;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class RatingValidator implements ConstraintValidator<ValidRating, Double> {

    private static final Set<Double> VALID_RATINGS = Set.of(
            1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0
    );

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return VALID_RATINGS.contains(value);
    }
}
