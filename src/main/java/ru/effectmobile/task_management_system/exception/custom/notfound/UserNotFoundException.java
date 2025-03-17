package ru.effectmobile.task_management_system.exception.custom.notfound;

public final class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
