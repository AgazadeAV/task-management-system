package ru.effectmobile.task_management_system.exception.util;

import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessageUtil {

    public static String getConstraintViolationMessage(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage() + ", not " + violation.getInvalidValue())
                .collect(Collectors.joining("\n"));
    }

    public static String getMethodArgumentNotValidMessage(MethodArgumentNotValidException ex) {
        var fieldError = ex.getBindingResult().getFieldError();
        if (fieldError == null) {
            return "Validation error with no specific field details";
        }

        return fieldError.getDefaultMessage();
    }

    public static class Messages {

        public static final String COMMENT_NOT_FOUND_BY_ID_MESSAGE = "Comment not found with id: %s";
        public static final String TASK_NOT_FOUND_BY_ID_MESSAGE = "Task not found with id: %s";
        public static final String USER_NOT_FOUND_BY_ID_MESSAGE = "User not found with id: %s";
        public static final String ENUM_VALUE_NULL_OR_EMPTY_MESSAGE = "Value cannot be null or empty for %s. Allowed values: %s";
        public static final String INVALID_ENUM_VALUE_MESSAGE = "Invalid value '%s' for: %s. Allowed values: %s";
        public static final String USER_NOT_FOUND_BY_EMAIL_MESSAGE = "User not found with email: %s";
        public static final String PASSWORD_DOESNT_MATCH_MESSAGE = "Invalid credentials";
        public static final String ACCESS_DENIED_MESSAGE = "Access denied: ";
        public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
        public static final String USER_NOT_FOUND_MESSAGE = "User not found";
        public static final String DB_CONSTRAINT_VIOLATION_MESSAGE = "Database constraint violation: ";
        public static final String MALFORMED_JSON_MESSAGE = "Malformed JSON request: ";
        public static final String MISSING_HEADER_MESSAGE = "Missing request header: ";
        public static final String EMAIL_ALREADY_REGISTERED = "Email: '%s' already registered";
        public static final String PHONE_NUMBER_ALREADY_REGISTERED = "Phone number '%s' already registered";
        public static final String USERNAME_ALREADY_REGISTERED = "Username: '%s' already registered";
    }
}
