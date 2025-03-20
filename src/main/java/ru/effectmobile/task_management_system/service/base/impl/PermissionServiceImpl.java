package ru.effectmobile.task_management_system.service.base.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.effectmobile.task_management_system.exception.custom.auth.UserDoesntHaveEnoughRightsException;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.service.base.PermissionService;

import java.util.UUID;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.COMMENT_DELETE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_CREATE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_DELETE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_UPDATE_FORBIDDEN_MESSAGE;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    public void checkCanDeleteComment(Comment comment, User user) {
        if (!user.getRole().equals(Role.ROLE_ADMIN) && !user.getId().equals(comment.getAuthor().getId())) {
            throw new UserDoesntHaveEnoughRightsException(COMMENT_DELETE_FORBIDDEN_MESSAGE);
        }
    }

    public void checkCanCreateTask(User user) {
        if (!user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new UserDoesntHaveEnoughRightsException(TASK_CREATE_FORBIDDEN_MESSAGE);
        }
    }

    public void checkCanDeleteTask(User user) {
        if (!user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new UserDoesntHaveEnoughRightsException(TASK_DELETE_FORBIDDEN_MESSAGE);
        }
    }

    public void checkCanUpdateTask(User user, UUID assigneeId) {
        if (!user.getRole().equals(Role.ROLE_ADMIN) && !user.getId().equals(assigneeId)) {
            throw new UserDoesntHaveEnoughRightsException(TASK_UPDATE_FORBIDDEN_MESSAGE);
        }
    }
}
