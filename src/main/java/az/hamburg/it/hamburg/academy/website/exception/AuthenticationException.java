package az.hamburg.it.hamburg.academy.website.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AuthenticationException extends RuntimeException {
    int httpStatus;
    String code;

    public AuthenticationException(String code, String message, int httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }
}