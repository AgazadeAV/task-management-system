package ru.effectmobile.task_management_system.service.base;

import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.User;

import java.util.UUID;

public interface PermissionService {

    void checkCanDeleteComment(Comment comment, User user);

    void checkCanCreateTask(User user);

    void checkCanDeleteTask(User user);

    void checkCanUpdateTask(User user, UUID assigneeId);
}
