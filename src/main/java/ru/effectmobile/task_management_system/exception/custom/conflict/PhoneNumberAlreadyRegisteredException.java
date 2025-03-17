package ru.effectmobile.task_management_system.exception.custom.conflict;

import ru.effectmobile.task_management_system.exception.custom.AlreadyRegisteredException;

public final class PhoneNumberAlreadyRegisteredException extends AlreadyRegisteredException {

    public PhoneNumberAlreadyRegisteredException(String message) {
        super(message);
    }
}
