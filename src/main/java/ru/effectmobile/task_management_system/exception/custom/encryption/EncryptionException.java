package ru.effectmobile.task_management_system.exception.custom.encryption;

import org.springframework.http.HttpStatus;
import ru.effectmobile.task_management_system.exception.custom.BusinessException;

public class EncryptionException extends BusinessException {

    public EncryptionException(String message, Throwable cause) {
        super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
