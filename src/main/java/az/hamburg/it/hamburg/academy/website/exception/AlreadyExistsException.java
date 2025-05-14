package az.hamburg.it.hamburg.academy.website.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AlreadyExistsException extends RuntimeException {
    String code;

    public AlreadyExistsException(String code, String message) {
        super(message);
        this.code = code;
    }
}
