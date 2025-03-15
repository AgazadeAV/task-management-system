package ru.effectmobile.task_management_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.model.Comment;

import java.util.UUID;

public interface CommentService extends AbstractService<Comment> {

    Page<Comment> findByTaskId(UUID taskId, Pageable pageable);
}
