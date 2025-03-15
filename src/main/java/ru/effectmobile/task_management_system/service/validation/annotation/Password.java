package ru.effectmobile.task_management_system.service.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.effectmobile.task_management_system.service.validation.PasswordValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default """
                Password must contain:
                - At least one number
                - At least one uppercase letter
                - At least one lowercase letter
                - At least one special symbol (e.g., ! # $ % & ' * + / = ? ^ _ ` { | } ~ -)
                - Only Latin characters
                - Be from 8 to 22 characters long
            """;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
