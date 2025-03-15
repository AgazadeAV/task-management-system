package ru.effectmobile.task_management_system.exception;

public class EmailAlreadyRegisteredException extends AlreadyRegisteredException {

    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
}
