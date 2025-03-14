package ru.effectmobile.task_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.exception.CommentNotFoundException;
import ru.effectmobile.task_management_system.model.Comment;
import ru.effectmobile.task_management_system.repository.CommentRepository;
import ru.effectmobile.task_management_system.service.CommentService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findById(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Comment comment = findById(id);
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByTaskId(UUID taskId) {
        return commentRepository.findByTaskId(taskId);
    }
}
