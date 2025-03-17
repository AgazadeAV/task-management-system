package ru.effectmobile.task_management_system.exception.custom.conflict;

public final class PhoneNumberAlreadyRegisteredException extends AlreadyRegisteredException {

    public PhoneNumberAlreadyRegisteredException(String message) {
        super(message);
    }
}
