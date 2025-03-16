package ru.effectmobile.task_management_system.exception.notfound;

import ru.effectmobile.task_management_system.exception.base.NotFoundException;

public final class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}
