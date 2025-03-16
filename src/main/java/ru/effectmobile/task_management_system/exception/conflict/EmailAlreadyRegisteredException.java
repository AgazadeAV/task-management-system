package ru.effectmobile.task_management_system.exception.conflict;

import ru.effectmobile.task_management_system.exception.base.AlreadyRegisteredException;

public final class EmailAlreadyRegisteredException extends AlreadyRegisteredException {

    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
