package ru.effectmobile.task_management_system.exception.custom.notfound;

public final class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}
