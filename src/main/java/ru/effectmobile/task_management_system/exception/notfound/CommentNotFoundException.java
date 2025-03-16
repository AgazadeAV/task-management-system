package ru.effectmobile.task_management_system.exception.notfound;

import ru.effectmobile.task_management_system.exception.base.NotFoundException;

public final class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException(String message) {
        super(message);
    }
}
