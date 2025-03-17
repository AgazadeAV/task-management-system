package ru.effectmobile.task_management_system.exception.custom.conflict;

import org.springframework.http.HttpStatus;
import ru.effectmobile.task_management_system.exception.custom.BusinessException;

public abstract class AlreadyRegisteredException extends BusinessException {

    protected AlreadyRegisteredException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
