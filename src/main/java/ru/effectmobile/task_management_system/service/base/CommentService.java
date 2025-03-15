package ru.effectmobile.task_management_system.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.model.entity.Comment;

import java.util.UUID;

public interface CommentService extends BaseService<Comment> {

    Page<Comment> findByTaskId(UUID taskId, Pageable pageable);
}
