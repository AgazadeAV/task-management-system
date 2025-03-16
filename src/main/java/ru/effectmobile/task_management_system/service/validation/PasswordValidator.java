package ru.effectmobile.task_management_system.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import ru.effectmobile.task_management_system.service.validation.annotation.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[~''`,|{}\\[\\]@$!%*?&#=/^+÷×€£¥₩:;\"()_-])(?=\\S+$)[0-9a-zA-Z~''`,|{}\\[\\]@$!%*?&#=/^+÷×€£¥₩:;\"()_-]{8,22}$";

    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(password)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password cannot be blank")
                    .addConstraintViolation();
            return false;
        }
        if (!password.matches(PASSWORD_PATTERN)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password does not meet security criteria")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
