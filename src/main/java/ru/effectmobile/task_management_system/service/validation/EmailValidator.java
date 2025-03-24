package ru.effectmobile.task_management_system.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.effectmobile.task_management_system.service.validation.annotation.Email;

public class EmailValidator implements ConstraintValidator<Email, String> {

    private static final String STRICT_EMAIL_REGEX =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Override
    public void initialize(Email constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return value.matches(STRICT_EMAIL_REGEX);
    }
}
