package ru.effectmobile.task_management_system.exception;

public abstract class AlreadyRegisteredException extends RuntimeException {

    public AlreadyRegisteredException(String message) {
        super(message);
    }
}
