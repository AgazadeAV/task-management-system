package ru.effectmobile.task_management_system.exception.conflict;

import ru.effectmobile.task_management_system.exception.base.AlreadyRegisteredException;

public final class PhoneNumberAlreadyRegisteredException extends AlreadyRegisteredException {

    public PhoneNumberAlreadyRegisteredException(String message) {
        super(message);
    }
}
