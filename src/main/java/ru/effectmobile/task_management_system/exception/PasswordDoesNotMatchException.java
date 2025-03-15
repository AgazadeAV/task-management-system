package ru.effectmobile.task_management_system.exception;

public class PasswordDoesNotMatchException extends RuntimeException {

    public PasswordDoesNotMatchException(String message) {
        super(message);
    }
}
