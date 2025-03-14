package ru.effectmobile.task_management_system.service;

import ru.effectmobile.task_management_system.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    List<Comment> findAll();

    Comment findById(UUID id);

    Comment save(Comment comment);

    void deleteById(UUID id);

    List<Comment> findByTaskId(UUID taskId);
}
