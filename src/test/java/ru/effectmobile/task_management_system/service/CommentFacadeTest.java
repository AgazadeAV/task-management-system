package ru.effectmobile.task_management_system.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.service.base.CommentService;
import ru.effectmobile.task_management_system.service.base.TaskService;
import ru.effectmobile.task_management_system.service.base.UserService;
import ru.effectmobile.task_management_system.service.facade.impl.CommentFacadeImpl;
import ru.effectmobile.task_management_system.service.factory.CommentFactory;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.mapper.CommentMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAuthorUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createComment;
import static ru.effectmobile.task_management_system.util.ModelCreator.createCommentRequestDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createCommentResponseDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createMetaData;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTask;

@ExtendWith(MockitoExtension.class)
class CommentFacadeTest {

    @Mock
    private CommentService commentService;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentFactory commentFactory;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @Mock
    private MetaDataFactory metaDataFactory;

    @InjectMocks
    private CommentFacadeImpl commentFacade;

    private final static Task TASK = createTask(TaskStatus.COMPLETED, TaskPriority.LOW);
    private final static User AUTHOR = createAuthorUser(Role.ROLE_ADMIN);
    private final static Comment COMMENT = createComment();
    private final static CommentResponseDTO COMMENT_RESPONSE_DTO = createCommentResponseDTO();
    private final static CommentRequestDTO COMMENT_REQUEST_DTO = createCommentRequestDTO();
    private final static MetaData META_DATA = createMetaData();

    @Test
    void getTaskComments_ShouldReturnComments() {
        Pageable pageable = mock(Pageable.class);
        Page<Comment> commentPage = new PageImpl<>(List.of(COMMENT));
        when(taskService.findById(TASK.getId())).thenReturn(TASK);
        when(commentService.findByTaskId(TASK.getId(), pageable)).thenReturn(commentPage);
        when(commentMapper.commentToResponseDTO(COMMENT)).thenReturn(COMMENT_RESPONSE_DTO);

        Page<CommentResponseDTO> result = commentFacade.getTaskComments(TASK.getId(), pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(COMMENT_RESPONSE_DTO, result.getContent().get(0));
        verify(taskService).findById(TASK.getId());
        verify(commentService).findByTaskId(TASK.getId(), pageable);
    }

    @Test
    void createComment_ShouldReturnSavedComment() {
        when(taskService.findById(TASK.getId())).thenReturn(TASK);
        when(userService.findById(AUTHOR.getId())).thenReturn(AUTHOR);
        when(metaDataFactory.createMetaData()).thenReturn(META_DATA);
        when(commentFactory.createComment(COMMENT_REQUEST_DTO, TASK, AUTHOR, META_DATA)).thenReturn(COMMENT);
        when(commentService.save(COMMENT)).thenReturn(COMMENT);
        when(commentMapper.commentToResponseDTO(COMMENT)).thenReturn(COMMENT_RESPONSE_DTO);

        CommentResponseDTO result = commentFacade.createComment(COMMENT_REQUEST_DTO);

        assertEquals(COMMENT_RESPONSE_DTO, result);
        verify(taskService).findById(TASK.getId());
        verify(userService).findById(AUTHOR.getId());
        verify(commentFactory).createComment(COMMENT_REQUEST_DTO, TASK, AUTHOR, META_DATA);
        verify(commentService).save(COMMENT);
    }

    @Test
    void deleteComment_ShouldDeleteSuccessfully() {
        doNothing().when(commentService).deleteById(COMMENT.getId());

        assertDoesNotThrow(() -> commentFacade.deleteComment(COMMENT.getId()));

        verify(commentService).deleteById(COMMENT.getId());
    }
}
