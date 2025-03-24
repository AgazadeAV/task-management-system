package ru.effectmobile.task_management_system.service.facade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.exception.custom.auth.UserDoesntHaveEnoughRightsException;
import ru.effectmobile.task_management_system.exception.custom.notfound.TaskNotFoundException;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.service.PermissionService;
import ru.effectmobile.task_management_system.service.TaskService;
import ru.effectmobile.task_management_system.service.UserService;
import ru.effectmobile.task_management_system.service.facade.impl.TaskFacadeImpl;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.factory.TaskFactory;
import ru.effectmobile.task_management_system.service.mapper.TaskMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_CREATE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_DELETE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_NOT_FOUND_BY_ID_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_UPDATE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAsigneeUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAuthorUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createMetaData;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTask;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTaskRequestDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTaskResponseDTO;

@ExtendWith(MockitoExtension.class)
class TaskFacadeTest {

    @Mock
    private TaskService taskService;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskFactory taskFactory;

    @Mock
    private UserService userService;

    @Mock
    private MetaDataFactory metaDataFactory;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private TaskFacadeImpl taskFacade;

    private static final User AUTHOR = createAuthorUser(Role.ROLE_ADMIN);
    private static final User ASSIGNEE = createAsigneeUser(Role.ROLE_USER);
    private static final Task TASK = createTask(TaskStatus.COMPLETED, TaskPriority.LOW, AUTHOR, ASSIGNEE);
    private static final TaskRequestDTO REQUEST_DTO = createTaskRequestDTO();
    private static final TaskResponseDTO RESPONSE_DTO = createTaskResponseDTO();
    private static final MetaData META_DATA = createMetaData();
    private static final Pageable PAGEABLE = mock(Pageable.class);

    @Test
    void getAllTasks_ShouldReturnTasks() {
        Pageable pageable = mock(Pageable.class);
        Page<Task> taskPage = new PageImpl<>(List.of(TASK));
        when(taskService.findAll(pageable)).thenReturn(taskPage);
        when(taskMapper.taskToResponseDTO(TASK)).thenReturn(RESPONSE_DTO);

        Page<TaskResponseDTO> result = taskFacade.getAllTasks(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(RESPONSE_DTO, result.getContent().get(0));
        verify(taskService).findAll(pageable);
    }

    @Test
    void getTaskById_ShouldReturnTask() {
        when(taskService.findById(TASK.getId())).thenReturn(TASK);
        when(taskMapper.taskToResponseDTO(TASK)).thenReturn(RESPONSE_DTO);

        TaskResponseDTO result = taskFacade.getTaskById(TASK.getId());

        assertEquals(RESPONSE_DTO, result);
        verify(taskService).findById(TASK.getId());
    }

    @Test
    void getTaskById_ShouldThrowException_WhenTaskNotFound() {
        when(taskService.findById(TASK.getId()))
                .thenThrow(new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, TASK.getId())));

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskFacade.getTaskById(TASK.getId()));

        verify(taskService).findById(TASK.getId());
        verify(taskMapper, never()).taskToResponseDTO(any());
    }

    @Test
    void createTask_ShouldReturnSavedTask() {
        when(metaDataFactory.createMetaData()).thenReturn(META_DATA);
        when(taskFactory.createTask(any(TaskRequestDTO.class), any(User.class), any(MetaData.class))).thenReturn(TASK);
        when(taskService.save(any(Task.class))).thenReturn(TASK);
        when(taskMapper.taskToResponseDTO(TASK)).thenReturn(RESPONSE_DTO);
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        doNothing().when(permissionService).checkCanCreateTask(AUTHOR);

        TaskResponseDTO result = taskFacade.createTask(REQUEST_DTO, AUTHOR.getEmail());

        assertNotNull(result);
        assertEquals(RESPONSE_DTO, result);
        verify(taskService).save(TASK);
        verify(taskMapper).taskToResponseDTO(TASK);
    }

    @Test
    void createTask_ShouldThrowException_WhenUserHasNoPermission() {
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        doThrow(new UserDoesntHaveEnoughRightsException(TASK_CREATE_FORBIDDEN_MESSAGE))
                .when(permissionService).checkCanCreateTask(AUTHOR);

        Assertions.assertThrows(UserDoesntHaveEnoughRightsException.class,
                () -> taskFacade.createTask(REQUEST_DTO, AUTHOR.getEmail()));

        verify(userService).findByEmail(AUTHOR.getEmail());
        verify(permissionService).checkCanCreateTask(AUTHOR);
        verifyNoInteractions(taskFactory, taskService, taskMapper);
    }


    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        when(taskService.findById(any(UUID.class))).thenReturn(TASK);
        doNothing().when(taskMapper).updateTaskFromRequestDTO(any(TaskRequestDTO.class), any(Task.class));
        when(taskService.save(any(Task.class))).thenReturn(TASK);
        when(taskMapper.taskToResponseDTO(any(Task.class))).thenReturn(RESPONSE_DTO);
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        doNothing().when(permissionService).checkCanUpdateTask(any(User.class), any(UUID.class));

        TaskResponseDTO result = taskFacade.updateTask(TASK.getId(), REQUEST_DTO, AUTHOR.getEmail());

        assertEquals(RESPONSE_DTO, result);
        verify(taskService).save(TASK);
    }

    @Test
    void updateTask_ShouldThrowException_WhenTaskNotFound() {
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        doNothing().when(permissionService).checkCanUpdateTask(AUTHOR, REQUEST_DTO.assigneeId());
        when(taskService.findById(TASK.getId()))
                .thenThrow(new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, TASK.getId())));

        Assertions.assertThrows(TaskNotFoundException.class,
                () -> taskFacade.updateTask(TASK.getId(), REQUEST_DTO, AUTHOR.getEmail()));

        verify(userService).findByEmail(AUTHOR.getEmail());
        verify(permissionService).checkCanUpdateTask(AUTHOR, REQUEST_DTO.assigneeId());
        verify(taskService).findById(TASK.getId());
        verify(taskMapper, never()).taskToResponseDTO(any());
    }

    @Test
    void updateTask_ShouldThrowException_WhenUserHasNoPermission() {
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        doThrow(new UserDoesntHaveEnoughRightsException(TASK_UPDATE_FORBIDDEN_MESSAGE))
                .when(permissionService).checkCanUpdateTask(AUTHOR, REQUEST_DTO.assigneeId());

        Assertions.assertThrows(UserDoesntHaveEnoughRightsException.class,
                () -> taskFacade.updateTask(TASK.getId(), REQUEST_DTO, AUTHOR.getEmail()));

        verify(userService).findByEmail(AUTHOR.getEmail());
        verify(permissionService).checkCanUpdateTask(AUTHOR, REQUEST_DTO.assigneeId());
        verify(taskService, never()).findById(any());
        verify(taskMapper, never()).taskToResponseDTO(any());
    }

    @Test
    void deleteTask_ShouldDeleteSuccessfully() {
        doNothing().when(taskService).deleteById(TASK.getId());
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        doNothing().when(permissionService).checkCanDeleteTask(AUTHOR);

        assertDoesNotThrow(() -> taskFacade.deleteTask(TASK.getId(), AUTHOR.getEmail()));

        verify(taskService).deleteById(TASK.getId());
    }

    @Test
    void deleteTask_ShouldThrowException_WhenUserHasNoPermission() {
        when(userService.findByEmail(AUTHOR.getEmail())).thenReturn(AUTHOR);
        doThrow(new UserDoesntHaveEnoughRightsException(TASK_DELETE_FORBIDDEN_MESSAGE))
                .when(permissionService).checkCanDeleteTask(AUTHOR);

        Assertions.assertThrows(UserDoesntHaveEnoughRightsException.class,
                () -> taskFacade.deleteTask(TASK.getId(), AUTHOR.getEmail()));

        verify(userService).findByEmail(AUTHOR.getEmail());
        verify(permissionService).checkCanDeleteTask(AUTHOR);
        verify(taskService, never()).deleteById(any());
    }


    @Test
    void getTasksWithFilters_ShouldReturnFilteredTasks() {
        Pageable pageable = mock(Pageable.class);
        TaskFilterDTO filterDTO = new TaskFilterDTO(AUTHOR.getId(), ASSIGNEE.getId(), null, null);
        Page<Task> taskPage = new PageImpl<>(List.of(TASK));
        when(taskService.findWithFilters(filterDTO, pageable)).thenReturn(taskPage);
        when(taskMapper.taskToResponseDTO(TASK)).thenReturn(RESPONSE_DTO);

        Page<TaskResponseDTO> result = taskFacade.getTasksWithFilters(filterDTO, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(RESPONSE_DTO, result.getContent().get(0));
        verify(taskService).findWithFilters(filterDTO, pageable);
    }

    @ParameterizedTest
    @MethodSource("ru.effectmobile.task_management_system.util.ModelCreator#provideFilters")
    void getTasksWithFilters_ShouldReturnFilteredTasks(TaskFilterDTO filterDTO, List<Task> expectedTasks, long expectedTotal) {
        Page<Task> taskPage = new PageImpl<>(expectedTasks);
        when(taskService.findWithFilters(filterDTO, PAGEABLE)).thenReturn(taskPage);

        if (!expectedTasks.isEmpty()) {
            when(taskMapper.taskToResponseDTO(any(Task.class))).thenReturn(RESPONSE_DTO);
        }

        Page<TaskResponseDTO> result = taskFacade.getTasksWithFilters(filterDTO, PAGEABLE);

        assertEquals(expectedTotal, result.getTotalElements());

        if (expectedTotal > 0) {
            assertEquals(RESPONSE_DTO, result.getContent().get(0));
            verify(taskMapper, atLeastOnce()).taskToResponseDTO(any(Task.class));
        } else {
            Assertions.assertTrue(result.getContent().isEmpty());
            verify(taskMapper, never()).taskToResponseDTO(any(Task.class));
        }

        verify(taskService).findWithFilters(filterDTO, PAGEABLE);
    }
}
