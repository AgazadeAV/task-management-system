package ru.effectmobile.task_management_system.exception.validation;

import ru.effectmobile.task_management_system.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public final class InvalidEnumValueException extends BusinessException {

    public InvalidEnumValueException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
