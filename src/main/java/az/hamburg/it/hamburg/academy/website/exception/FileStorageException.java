package az.hamburg.it.hamburg.academy.website.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FileStorageException extends RuntimeException {
    String code;
    public FileStorageException(String code, String message) {
        super(message);
        this.code = code;
    }
}
