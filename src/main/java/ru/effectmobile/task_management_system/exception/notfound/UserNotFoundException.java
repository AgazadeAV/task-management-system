package ru.effectmobile.task_management_system.exception.notfound;

import ru.effectmobile.task_management_system.exception.base.NotFoundException;

public final class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
