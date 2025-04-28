package az.hamburg.it.hamburg.academy.website.exception;

import lombok.Getter;

@Getter
public class MinioInitializationException extends RuntimeException {
    private final String code;
    public MinioInitializationException(String code, String message) {
        super(message);
        this.code = code;
    }
}
