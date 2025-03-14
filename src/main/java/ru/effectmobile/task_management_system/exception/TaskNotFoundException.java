package ru.effectmobile.task_management_system.exception;

public class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}
