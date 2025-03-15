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
        public static final String ENUM_VALUE_NULL_OR_EMPTY = "Value cannot be null or empty for %s. Allowed values: %s";
        public static final String INVALID_ENUM_VALUE = "Invalid value '%s' for: %s. Allowed values: %s";
    }
}
