package ru.effectmobile.task_management_system.service.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.effectmobile.task_management_system.config.crypto.CipherService;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.service.CommentService;
import ru.effectmobile.task_management_system.service.PermissionService;
import ru.effectmobile.task_management_system.service.TaskService;
import ru.effectmobile.task_management_system.service.UserService;
import ru.effectmobile.task_management_system.service.facade.CommentFacade;
import ru.effectmobile.task_management_system.service.factory.CommentFactory;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.mapper.CommentMapper;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class CommentFacadeImpl implements CommentFacade {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final CommentFactory commentFactory;
    private final TaskService taskService;
    private final UserService userService;
    private final MetaDataFactory metaDataFactory;
    private final CipherService cipherService;
    private final PermissionService permissionService;

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
    public CommentResponseDTO createComment(CommentRequestDTO commentDTO, String email) {
        log.info("Creating a comment for task ID: {} by user email: {}", commentDTO.taskId(), cipherService.decrypt(email));

        Task task = taskService.findById(commentDTO.taskId());
        User author = userService.findByEmail(email);
        MetaData metaData = metaDataFactory.createMetaData();
        Comment comment = commentFactory.createComment(commentDTO, task, author, metaData);

        Comment savedComment = commentService.save(comment);
        log.info("Comment created successfully with ID: {}", savedComment.getId());

        return commentMapper.commentToResponseDTO(savedComment);
    }

    @Override
    @Transactional
    public void deleteComment(UUID id, String email) {
        User user = userService.findByEmail(email);
        Comment comment = commentService.findById(id);
        permissionService.checkCanDeleteComment(comment, user);
        log.warn("Deleting comment with ID: {}", id);
        commentService.deleteById(id);
        log.info("Comment deleted successfully with ID: {}", id);
    }
}
