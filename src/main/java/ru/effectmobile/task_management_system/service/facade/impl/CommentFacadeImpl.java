package ru.effectmobile.task_management_system.service.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.exception.custom.auth.UserDoesntHaveEnoughRightsException;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.service.base.CipherService;
import ru.effectmobile.task_management_system.service.base.CommentService;
import ru.effectmobile.task_management_system.service.base.TaskService;
import ru.effectmobile.task_management_system.service.base.UserService;
import ru.effectmobile.task_management_system.service.facade.CommentFacade;
import ru.effectmobile.task_management_system.service.factory.CommentFactory;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.mapper.CommentMapper;

import java.util.UUID;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.COMMENT_DELETE_FORBIDDEN_MESSAGE;

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
    private final CipherService cipherService;

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
        canUserDeleteComment(id, email);
        log.warn("Deleting comment with ID: {}", id);
        commentService.deleteById(id);
        log.info("Comment deleted successfully with ID: {}", id);
    }

    private void canUserDeleteComment(UUID id, String email) {
        User user = userService.findByEmail(email);
        Role role = user.getRole();
        Comment comment = commentService.findById(id);
        if (!role.equals(Role.ROLE_ADMIN)) {
            if (!user.getId().equals(comment.getAuthor().getId())) {
                throw new UserDoesntHaveEnoughRightsException(COMMENT_DELETE_FORBIDDEN_MESSAGE);
            }
        }
    }
}
