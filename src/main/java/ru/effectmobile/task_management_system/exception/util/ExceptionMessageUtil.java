package ru.effectmobile.task_management_system.exception.util;

import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessageUtil {

    public static String getConstraintViolationMessage(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(violation -> String.format(Messages.CONSTRAINT_VIOLATION_MESSAGE,
                        violation.getPropertyPath(),
                        violation.getMessage(),
                        violation.getInvalidValue()))
                .collect(Collectors.joining("\n"));
    }

    public static String getMethodArgumentNotValidMessage(MethodArgumentNotValidException ex) {
        var fieldError = ex.getBindingResult().getFieldError();
        return fieldError != null
                ? fieldError.getDefaultMessage()
                : Messages.VALIDATION_ERROR_NO_DETAILS;
    }

    public static String getHandlerMethodValidationMessage(HandlerMethodValidationException ex) {
        return ex.getParameterValidationResults().stream()
                .map(result -> {
                    MethodParameter param = result.getMethodParameter();
                    String paramName = param.getParameterName();
                    String message = result.getResolvableErrors().stream()
                            .map(MessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.joining("; "));

                    return String.format(Messages.HANDLER_VALIDATION_ERROR_MESSAGE, paramName, message);
                })
                .collect(Collectors.joining("\n"));
    }

    public static String getMissingRequestHeaderMessage(MissingRequestHeaderException ex) {
        String typeName = ex.getParameter().getNestedParameterType().getSimpleName();
        String reason = ex.isMissingAfterConversion()
                ? Messages.HEADER_PRESENT_BUT_NULL
                : Messages.HEADER_NOT_PRESENT;

        return String.format(Messages.MISSING_HEADER_MESSAGE, ex.getHeaderName(), typeName, reason);
    }

    public static <T extends Exception> String getExceptionMessage(T ex, String customMessage) {
        String message = customMessage + ex.getMessage();
        if (ex.getCause() != null) {
            message += String.format(Messages.EXCEPTION_REASON_MESSAGE, ex.getCause().getMessage());
        }
        return message;
    }

    public static class Messages {

        public static final String CONSTRAINT_VIOLATION_MESSAGE = "%s: %s, not %s";
        public static final String VALIDATION_ERROR_NO_DETAILS = "Validation error with no specific field details";
        public static final String HANDLER_VALIDATION_ERROR_MESSAGE = "Parameter '%s': %s";
        public static final String MISSING_HEADER_MESSAGE = "Missing request header: Required header '%s' (Expected type: %s) is %s.";
        public static final String HEADER_PRESENT_BUT_NULL = "present but converted to null";
        public static final String HEADER_NOT_PRESENT = "not present";
        public static final String EXCEPTION_REASON_MESSAGE = " (Reason: %s)";
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
        public static final String EMAIL_ALREADY_REGISTERED = "Email: '%s' already registered";
        public static final String PHONE_NUMBER_ALREADY_REGISTERED = "Phone number: '%s' already registered";
        public static final String USERNAME_ALREADY_REGISTERED = "Username: '%s' already registered";
        public static final String JWT_DECODING_ERROR_MESSAGE = "Invalid JWT token format: %s";
        public static final String JWT_ERROR_MESSAGE = "JWT processing error: %s";
    }
}
