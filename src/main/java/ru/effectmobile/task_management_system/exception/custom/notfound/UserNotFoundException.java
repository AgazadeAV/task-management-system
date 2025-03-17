package ru.effectmobile.task_management_system.exception.custom.notfound;

import ru.effectmobile.task_management_system.exception.custom.NotFoundException;

public final class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
