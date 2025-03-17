package ru.effectmobile.task_management_system.exception.custom.notfound;

import ru.effectmobile.task_management_system.exception.custom.NotFoundException;

public final class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException(String message) {
        super(message);
    }
}
