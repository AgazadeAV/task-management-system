package ru.effectmobile.task_management_system.exception.custom.auth;

import org.springframework.http.HttpStatus;
import ru.effectmobile.task_management_system.exception.custom.BusinessException;

public final class AdminAccessDeniedException extends BusinessException {

    public AdminAccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
