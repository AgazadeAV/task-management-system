package ru.effectmobile.task_management_system.exception.custom.notfound;

import ru.effectmobile.task_management_system.exception.custom.NotFoundException;

public final class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}
