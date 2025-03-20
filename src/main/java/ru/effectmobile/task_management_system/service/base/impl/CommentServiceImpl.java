package ru.effectmobile.task_management_system.service.base.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.exception.custom.notfound.CommentNotFoundException;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.repository.CommentRepository;
import ru.effectmobile.task_management_system.service.base.CommentService;

import java.util.UUID;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.COMMENT_NOT_FOUND_BY_ID_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Page<Comment> findAll(Pageable pageable) {
        log.debug("Fetching all comments with pagination: {}", pageable);
        return commentRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "commentsById", key = "#id")
    @Transactional(readOnly = true)
    public Comment findById(UUID id) {
        log.debug("Fetching comment by id: {}", id);
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Comment not found with id: {}", id);
                    return new CommentNotFoundException(String.format(COMMENT_NOT_FOUND_BY_ID_MESSAGE, id));
                });
    }

    @Override
    @CacheEvict(value = {"commentsById", "commentsByTask"}, key = "#comment.id")
    @Transactional
    public Comment save(Comment comment) {
        log.debug("Saving comment: {}", comment);
        return commentRepository.save(comment);
    }

    @Override
    @CacheEvict(value = {"commentsById", "commentsByTask"}, key = "#id")
    @Transactional
    public void deleteById(UUID id) {
        log.debug("Deleting comment by id: {}", id);
        Comment comment = findById(id);
        commentRepository.delete(comment);
        log.info("Deleted comment with id: {}", id);
    }

    @Override
    @Cacheable(value = "commentsByTask", key = "#taskId.toString() + #pageable.toString()")
    @Transactional(readOnly = true)
    public Page<Comment> findByTaskId(UUID taskId, Pageable pageable) {
        log.debug("Fetching comments for task id: {} with pagination: {}", taskId, pageable);
        return commentRepository.findByTaskId(taskId, pageable);
    }
}
