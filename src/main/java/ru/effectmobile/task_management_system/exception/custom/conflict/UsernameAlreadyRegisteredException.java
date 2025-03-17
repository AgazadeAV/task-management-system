package ru.effectmobile.task_management_system.exception.custom.conflict;

public final class UsernameAlreadyRegisteredException extends AlreadyRegisteredException {

    public UsernameAlreadyRegisteredException(String message) {
        super(message);
    }
}
