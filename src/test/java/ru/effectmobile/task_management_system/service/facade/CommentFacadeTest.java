package ru.effectmobile.task_management_system.service.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.config.crypto.CipherService;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.exception.custom.auth.UserDoesntHaveEnoughRightsException;
import ru.effectmobile.task_management_system.exception.custom.notfound.CommentNotFoundException;
import ru.effectmobile.task_management_system.exception.custom.notfound.TaskNotFoundException;
import ru.effectmobile.task_management_system.exception.custom.notfound.UserNotFoundException;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.service.base.CommentService;
import ru.effectmobile.task_management_system.service.base.PermissionService;
import ru.effectmobile.task_management_system.service.base.TaskService;
import ru.effectmobile.task_management_system.service.base.UserService;
import ru.effectmobile.task_management_system.service.facade.impl.CommentFacadeImpl;
import ru.effectmobile.task_management_system.service.factory.CommentFactory;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.mapper.CommentMapper;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.COMMENT_DELETE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.COMMENT_NOT_FOUND_BY_ID_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_NOT_FOUND_BY_ID_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USER_NOT_FOUND_BY_ID_MESSAGE;
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

    @Mock
    private CipherService cipherService;

    @Mock
    private PermissionService permissionService;

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
    void getTaskComments_ShouldThrowException_WhenTaskNotFound() {
        Pageable pageable = mock(Pageable.class);
        when(taskService.findById(TASK.getId()))
                .thenThrow(new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, TASK.getId())));

        assertThrows(TaskNotFoundException.class, () -> commentFacade.getTaskComments(TASK.getId(), pageable));

        verify(taskService).findById(TASK.getId());
        verify(commentService, never()).findByTaskId(any(), any());
    }

    @Test
    void createComment_ShouldReturnSavedComment() {
        when(taskService.findById(TASK.getId())).thenReturn(TASK);
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        when(metaDataFactory.createMetaData()).thenReturn(META_DATA);
        when(commentFactory.createComment(COMMENT_REQUEST_DTO, TASK, AUTHOR, META_DATA)).thenReturn(COMMENT);
        when(commentService.save(COMMENT)).thenReturn(COMMENT);
        when(commentMapper.commentToResponseDTO(COMMENT)).thenReturn(COMMENT_RESPONSE_DTO);
        when(cipherService.decrypt(AUTHOR.getEmail())).thenReturn(AUTHOR.getEmail());

        CommentResponseDTO result = commentFacade.createComment(COMMENT_REQUEST_DTO, AUTHOR.getEmail());

        assertEquals(COMMENT_RESPONSE_DTO, result);
        verify(taskService).findById(TASK.getId());
        verify(userService).findByEmail(AUTHOR.getEmail());
        verify(commentFactory).createComment(COMMENT_REQUEST_DTO, TASK, AUTHOR, META_DATA);
        verify(commentService).save(COMMENT);
    }

    @Test
    void createComment_ShouldThrowException_WhenTaskNotFound() {
        when(taskService.findById(COMMENT_REQUEST_DTO.taskId()))
                .thenThrow(new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, TASK.getId())));

        assertThrows(TaskNotFoundException.class, () -> commentFacade.createComment(COMMENT_REQUEST_DTO, AUTHOR.getEmail()));

        verify(taskService).findById(COMMENT_REQUEST_DTO.taskId());
        verify(userService, never()).findByEmail(any());
    }

    @Test
    void createComment_ShouldThrowException_WhenUserNotFound() {
        when(taskService.findById(COMMENT_REQUEST_DTO.taskId())).thenReturn(TASK);
        when(userService.findByEmail(AUTHOR.getEmail()))
                .thenThrow(new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MESSAGE, AUTHOR.getId())));

        assertThrows(UserNotFoundException.class, () -> commentFacade.createComment(COMMENT_REQUEST_DTO, AUTHOR.getEmail()));

        verify(taskService).findById(COMMENT_REQUEST_DTO.taskId());
        verify(userService).findByEmail(AUTHOR.getEmail());
        verify(commentService, never()).save(any());
    }

    @Test
    void deleteComment_ShouldDeleteSuccessfully() {
        doNothing().when(commentService).deleteById(COMMENT.getId());
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        when(commentService.findById(COMMENT.getId())).thenReturn(COMMENT);
        doNothing().when(permissionService).checkCanDeleteComment(COMMENT, AUTHOR);

        assertDoesNotThrow(() -> commentFacade.deleteComment(COMMENT.getId(), AUTHOR.getEmail()));

        verify(commentService).deleteById(COMMENT.getId());
    }

    @Test
    void deleteComment_ShouldThrowException_WhenCommentNotFound() {
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        when(commentService.findById(COMMENT.getId()))
                .thenThrow(new CommentNotFoundException(String.format(COMMENT_NOT_FOUND_BY_ID_MESSAGE, COMMENT.getId())));

        assertThrows(CommentNotFoundException.class, () -> commentFacade.deleteComment(COMMENT.getId(), AUTHOR.getEmail()));

        verify(userService).findByEmail(AUTHOR.getEmail());
        verify(commentService).findById(COMMENT.getId());
        verify(permissionService, never()).checkCanDeleteComment(any(), any());
    }

    @Test
    void deleteComment_ShouldThrowException_WhenForbidden() {
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        when(commentService.findById(COMMENT.getId())).thenReturn(COMMENT);
        doThrow(new UserDoesntHaveEnoughRightsException(COMMENT_DELETE_FORBIDDEN_MESSAGE))
                .when(permissionService).checkCanDeleteComment(COMMENT, AUTHOR);

        assertThrows(UserDoesntHaveEnoughRightsException.class, () -> commentFacade.deleteComment(COMMENT.getId(), AUTHOR.getEmail()));

        verify(commentService, never()).deleteById(any());
    }
}
