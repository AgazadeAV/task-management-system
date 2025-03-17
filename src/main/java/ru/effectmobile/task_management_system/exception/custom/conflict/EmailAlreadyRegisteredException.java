package ru.effectmobile.task_management_system.exception.custom.conflict;

public final class EmailAlreadyRegisteredException extends AlreadyRegisteredException {

    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
