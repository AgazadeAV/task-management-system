package ru.effectmobile.task_management_system.exception.conflict;

import ru.effectmobile.task_management_system.exception.base.AlreadyRegisteredException;

public final class UsernameAlreadyRegisteredException extends AlreadyRegisteredException {

    public UsernameAlreadyRegisteredException(String message) {
        super(message);
    }
}
