package ru.effectmobile.task_management_system.service.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.service.base.CommentService;
import ru.effectmobile.task_management_system.service.base.TaskService;
import ru.effectmobile.task_management_system.service.base.UserService;
import ru.effectmobile.task_management_system.service.facade.CommentFacade;
import ru.effectmobile.task_management_system.service.factory.CommentFactory;
import ru.effectmobile.task_management_system.service.mapper.CommentMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentFacadeImpl implements CommentFacade {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final CommentFactory commentFactory;
    private final TaskService taskService;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponseDTO> getTaskComments(UUID taskId, Pageable pageable) {
        taskService.findById(taskId);
        return commentService.findByTaskId(taskId, pageable)
                .map(commentMapper::commentToResponseDTO);
    }

    @Override
    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO commentDTO) {
        Task task = taskService.findById(commentDTO.taskId());
        User author = userService.findById(commentDTO.authorId());
        Comment comment = commentFactory.createComment(commentDTO, task, author);
        return commentMapper.commentToResponseDTO(commentService.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(UUID id) {
        commentService.deleteById(id);
    }
}
