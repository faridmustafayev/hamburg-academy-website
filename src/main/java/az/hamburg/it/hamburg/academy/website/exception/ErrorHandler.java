package az.hamburg.it.hamburg.academy.website.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.HTTP_METHOD_IS_NOT_CORRECT;
import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.UNEXPECTED_EXCEPTION;
import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.USER_ALREADY_EXISTS;
import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.VALIDATION_EXCEPTION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception ex) {
        log.error("UnexpectedException: ", ex);
        return ErrorResponse.builder()
                .code(UNEXPECTED_EXCEPTION.getCode())
                .message(UNEXPECTED_EXCEPTION.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handle(NotFoundException ex) {
        log.error("NotFoundException: ", ex);
        return ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handle(AlreadyExistsException ex) {
        log.error("AlreadyExistsException: ", ex);
        return ErrorResponse.builder()
                .code(USER_ALREADY_EXISTS.getCode())
                .message(USER_ALREADY_EXISTS.getMessage())
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorResponse handle(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException: ",ex);
        return ErrorResponse.builder()
                .code(HTTP_METHOD_IS_NOT_CORRECT.getCode())
                .message(HTTP_METHOD_IS_NOT_CORRECT.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handle(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: ", ex);
        List<ValidationException> exceptions = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationException(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return ErrorResponse.builder()
                .code(VALIDATION_EXCEPTION.getCode())
                .message(VALIDATION_EXCEPTION.getMessage())
                .validationExceptions(exceptions)
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handle(AuthenticationException ex) {
        log.error("AuthException: ", ex);
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ErrorResponse(ex.getCode(), ex.getMessage(), null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handle(BadCredentialsException ex) {
        log.error("BadCredentialsException: ", ex);
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorResponse handle(AccessDeniedException ex) {
        log.error("AccessDeniedException: ", ex);
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
    }
}
