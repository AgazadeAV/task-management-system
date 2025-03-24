package ru.effectmobile.task_management_system.service.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.effectmobile.task_management_system.service.validation.EmailValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default "Invalid email format. Must contain '@', a dot in the domain, and only Latin characters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
