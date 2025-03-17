package ru.effectmobile.task_management_system.exception.custom.notfound;

import org.springframework.http.HttpStatus;
import ru.effectmobile.task_management_system.exception.custom.BusinessException;

public abstract class NotFoundException extends BusinessException {

    protected NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
