package ru.effectmobile.task_management_system.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.service.base.TaskService;
import ru.effectmobile.task_management_system.service.base.UserService;
import ru.effectmobile.task_management_system.service.facade.impl.TaskFacadeImpl;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.factory.TaskFactory;
import ru.effectmobile.task_management_system.service.mapper.TaskMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_PRIORITY_EXAMPLE_ENUM;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_STATUS_EXAMPLE_ENUM;
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

    @InjectMocks
    private TaskFacadeImpl taskFacade;

    private static final User AUTHOR = createAuthorUser(Role.ROLE_ADMIN);
    private static final User ASSIGNEE = createAsigneeUser(Role.ROLE_USER);
    private static final Task TASK = createTask(TaskStatus.COMPLETED, TaskPriority.LOW, AUTHOR, ASSIGNEE);
    private static final TaskRequestDTO REQUEST_DTO = createTaskRequestDTO();
    private static final TaskResponseDTO RESPONSE_DTO = createTaskResponseDTO();
    private static final MetaData META_DATA = createMetaData();
    private static final Pageable PAGEABLE = mock(Pageable.class);
    private static final List<Task> TASKS = Arrays.asList(TASK, TASK);
    private static final List<Task> EMPTY_TASKS = Collections.emptyList();

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
    void createTask_ShouldReturnSavedTask() {
        when(userService.findById(any(UUID.class))).thenReturn(AUTHOR);
        when(metaDataFactory.createMetaData()).thenReturn(META_DATA);
        when(taskFactory.createTask(any(TaskRequestDTO.class), any(User.class), any(MetaData.class))).thenReturn(TASK);
        when(taskService.save(any(Task.class))).thenReturn(TASK);
        when(taskMapper.taskToResponseDTO(TASK)).thenReturn(RESPONSE_DTO);

        TaskResponseDTO result = taskFacade.createTask(REQUEST_DTO);

        assertNotNull(result);
        assertEquals(RESPONSE_DTO, result);
        verify(taskService).save(TASK);
        verify(taskMapper).taskToResponseDTO(TASK);
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        when(taskService.findById(any(UUID.class))).thenReturn(TASK);
        doNothing().when(taskMapper).updateTaskFromRequestDTO(any(TaskRequestDTO.class), any(Task.class));
        when(taskService.save(any(Task.class))).thenReturn(TASK);
        when(taskMapper.taskToResponseDTO(any(Task.class))).thenReturn(RESPONSE_DTO);

        TaskResponseDTO result = taskFacade.updateTask(TASK.getId(), REQUEST_DTO);

        assertEquals(RESPONSE_DTO, result);
        verify(taskService).save(TASK);
    }

    @Test
    void deleteTask_ShouldDeleteSuccessfully() {
        doNothing().when(taskService).deleteById(TASK.getId());

        assertDoesNotThrow(() -> taskFacade.deleteTask(TASK.getId()));

        verify(taskService).deleteById(TASK.getId());
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
    @MethodSource("provideFilters")
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

    static Stream<Arguments> provideFilters() {
        return Stream.of(
                Arguments.of(new TaskFilterDTO(null, null, null, null), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(AUTHOR.getId(), null, null, null), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(null, ASSIGNEE.getId(), null, null), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(null, null, TASK_STATUS_EXAMPLE_ENUM, null), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(null, null, null, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(AUTHOR.getId(), ASSIGNEE.getId(), null, null), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(AUTHOR.getId(), null, TASK_STATUS_EXAMPLE_ENUM, null), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(AUTHOR.getId(), null, null, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(null, ASSIGNEE.getId(), TASK_STATUS_EXAMPLE_ENUM, null), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(null, ASSIGNEE.getId(), null, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(null, null, TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(AUTHOR.getId(), ASSIGNEE.getId(), TASK_STATUS_EXAMPLE_ENUM, null), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(AUTHOR.getId(), ASSIGNEE.getId(), null, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(AUTHOR.getId(), null, TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(null, ASSIGNEE.getId(), TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(AUTHOR.getId(), ASSIGNEE.getId(), TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(new TaskFilterDTO(AUTHOR.getId(), ASSIGNEE.getId(), TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), EMPTY_TASKS, EMPTY_TASKS.size())
        );
    }
}
