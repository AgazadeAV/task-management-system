package ru.effectmobile.task_management_system.exception.base;

import org.springframework.http.HttpStatus;

public abstract class AlreadyRegisteredException extends BusinessException {

    protected AlreadyRegisteredException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
