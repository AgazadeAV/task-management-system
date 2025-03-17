package ru.effectmobile.task_management_system.exception.custom.validation;

import ru.effectmobile.task_management_system.exception.custom.BusinessException;
import org.springframework.http.HttpStatus;

public final class InvalidEnumValueException extends BusinessException {

    public InvalidEnumValueException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
