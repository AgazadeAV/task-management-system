package ru.effectmobile.task_management_system.exception.custom.auth;

import org.springframework.http.HttpStatus;
import ru.effectmobile.task_management_system.exception.custom.BusinessException;

public final class UserDoesntHaveEnoughRightsException extends BusinessException {

    public UserDoesntHaveEnoughRightsException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
