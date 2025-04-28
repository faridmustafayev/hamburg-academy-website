package az.hamburg.it.hamburg.academy.website.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ExceptionConstants {
    UNEXPECTED_EXCEPTION("UNEXPECTED_EXCEPTION", "Unexpected exception occurred"),
    HTTP_METHOD_IS_NOT_CORRECT("HTTP_METHOD_IS_NOT_CORRECT", "Http method is not correct"),
    SPECIALIZATION_NOT_FOUND("SPECIALIZATION_NOT_FOUND", "No specialization with id was found"),
    REVIEW_NOT_FOUND("REVIEW_NOT_FOUND", "no review with id was found"),
    GRADUATE_NOT_FOUND("GRADUATE_NOT_FOUND", "no graduate with id was found"),
    COURSE_REQUEST_NOT_FOUND("COURSE_REQUEST_NOT_FOUND", "no course request with id was found"),
    COURSE_IMAGE_NOT_FOUND("COURSE_IMAGE_NOT_FOUND", "no course with id was found"),
    CONTACT_NOT_FOUND("CONTACT_NOT_FOUND", "no contact with id was found"),
    SERVICE_NOT_FOUND("SERVICE_NOT_FOUND", "no service with id was found"),
    SYLLABUS_NOT_FOUND("SYLLABUS_NOT_FOUND", "no syllabus with id was found"),
    PROJECT_REQUEST_NOT_FOUND("PROJECT_REQUEST_NOT_FOUND", "no project request with id was found"),
    INSTRUCTOR_NOT_FOUND("INSTRUCTOR_NOT_FOUND", "No instructor with id was found"),
    FILE_NOT_FOUND("FILE_NOT_FOUND", "No file with id was found"),
    FILE_NOT_DELETED("FILE_NOT_DELETED", "No file with id was deleted"),
    FILE_NOT_UPLOADED("FILE_NOT_UPLOADED", "No file with id was uploaded"),
    USER_NOT_FOUND("USER_NOT_FOUND", "no user with id was found"),
    DIPLOMA_NOT_FOUND("DIPLOMA_NOT_FOUND", "no diploma with id was found"),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "User already exist"),
    USER_UNAUTHORIZED("USER_UNAUTHORIZED", "User unauthorized"),
    REFRESH_TOKEN_EXPIRED("REFRESH_TOKEN_EXPIRED", "Refresh token expired"),
    REFRESH_TOKEN_COUNT_EXPIRED("REFRESH_TOKEN_COUNT_EXPIRED", "Refresh token count expired"),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "Token expired"),
    VALIDATION_EXCEPTION("VALIDATION_EXCEPTION", "Validation exception"),
    MINIO_INITIALIZATION_EXCEPTION("MINIO_INITIALIZATION_EXCEPTION", "Failed to initialize buckets");

    private final String code;
    private final String message;

    public String getMessage(Long id) {
        if ((this == SPECIALIZATION_NOT_FOUND || this == INSTRUCTOR_NOT_FOUND
                || this == GRADUATE_NOT_FOUND || this == COURSE_IMAGE_NOT_FOUND
                || this == COURSE_REQUEST_NOT_FOUND || this == REVIEW_NOT_FOUND
                || this == PROJECT_REQUEST_NOT_FOUND || this == CONTACT_NOT_FOUND
                || this == SERVICE_NOT_FOUND || this == SYLLABUS_NOT_FOUND
                || this == FILE_NOT_FOUND || this == FILE_NOT_UPLOADED
                || this == FILE_NOT_DELETED || this == USER_NOT_FOUND
                || this == DIPLOMA_NOT_FOUND) && id != null) {
            return String.format("No %s with id (ID: %s) was found",
                    this.name().toLowerCase().replace("_not_found", ""), id);
        }
        return this.message;
    }

    public String getMessage(List<Long> id) {
        if ((this == SPECIALIZATION_NOT_FOUND) && id != null) {
            return String.format("No %s with id (ID: %s) was found",
                    this.name().toLowerCase().replace("_not_found", ""), id);
        }
        return this.message;
    }
}
