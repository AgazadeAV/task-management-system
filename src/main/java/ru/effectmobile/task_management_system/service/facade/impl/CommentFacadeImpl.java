package ru.effectmobile.task_management_system.service.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.service.base.CommentService;
import ru.effectmobile.task_management_system.service.base.TaskService;
import ru.effectmobile.task_management_system.service.base.UserService;
import ru.effectmobile.task_management_system.service.facade.CommentFacade;
import ru.effectmobile.task_management_system.service.factory.CommentFactory;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.mapper.CommentMapper;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentFacadeImpl implements CommentFacade {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final CommentFactory commentFactory;
    private final TaskService taskService;
    private final UserService userService;
    private final MetaDataFactory metaDataFactory;

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponseDTO> getTaskComments(UUID taskId, Pageable pageable) {
        log.debug("Fetching comments for task ID: {}", taskId);
        taskService.findById(taskId);
        Page<CommentResponseDTO> comments = commentService.findByTaskId(taskId, pageable)
                .map(commentMapper::commentToResponseDTO);
        log.debug("Found {} comments for task ID: {}", comments.getTotalElements(), taskId);
        return comments;
    }

    @Override
    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO commentDTO) {
        log.info("Creating a comment for task ID: {} by user ID: {}", commentDTO.taskId(), commentDTO.authorId());

        Task task = taskService.findById(commentDTO.taskId());
        User author = userService.findById(commentDTO.authorId());
        MetaData metaData = metaDataFactory.createMetaData();
        Comment comment = commentFactory.createComment(commentDTO, task, author, metaData);

        Comment savedComment = commentService.save(comment);
        log.info("Comment created successfully with ID: {}", savedComment.getId());

        return commentMapper.commentToResponseDTO(savedComment);
    }

    @Override
    @Transactional
    public void deleteComment(UUID id) {
        log.warn("Deleting comment with ID: {}", id);
        commentService.deleteById(id);
        log.info("Comment deleted successfully with ID: {}", id);
    }
}
