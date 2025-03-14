package ru.effectmobile.task_management_system.util;

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
}
