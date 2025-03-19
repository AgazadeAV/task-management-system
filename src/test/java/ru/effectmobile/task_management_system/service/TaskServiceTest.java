package ru.effectmobile.task_management_system.service;

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
import org.springframework.data.domain.PageRequest;
import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.exception.custom.notfound.TaskNotFoundException;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.repository.TaskRepository;
import ru.effectmobile.task_management_system.service.base.impl.TaskServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ASSIGNEE_ID_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.AUTHOR_ID_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_PRIORITY_EXAMPLE_ENUM;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_STATUS_EXAMPLE_ENUM;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTask;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTaskFilterDTO;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    private static final PageRequest PAGEABLE = PageRequest.of(0, 10);
    private static final Task TASK = createTask(TaskStatus.COMPLETED, TaskPriority.LOW);
    private static final List<Task> TASKS = Arrays.asList(TASK, TASK);
    private static final List<Task> EMPTY_TASKS = Collections.emptyList();

    @Test
    void findAll_ShouldReturnPageOfTasks() {
        Page<Task> page = new PageImpl<>(List.of(TASK));
        when(taskRepository.findAll(PAGEABLE)).thenReturn(page);

        Page<Task> result = taskService.findAll(PAGEABLE);

        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findAll(PAGEABLE);
    }

    @Test
    void findById_ShouldReturnTask_WhenExists() {
        when(taskRepository.findById(TASK.getId())).thenReturn(Optional.of(TASK));

        Task result = taskService.findById(TASK.getId());

        assertEquals(TASK.getId(), result.getId());
        verify(taskRepository).findById(TASK.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(taskRepository.findById(TASK.getId())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.findById(TASK.getId()));
        verify(taskRepository).findById(TASK.getId());
    }

    @Test
    void save_ShouldReturnSavedTask() {
        when(taskRepository.save(TASK)).thenReturn(TASK);

        Task result = taskService.save(TASK);

        assertEquals(TASK, result);
        verify(taskRepository).save(TASK);
    }

    @Test
    void deleteById_ShouldDeleteTask_WhenExists() {
        when(taskRepository.findById(TASK.getId())).thenReturn(Optional.of(TASK));
        doNothing().when(taskRepository).delete(TASK);

        assertDoesNotThrow(() -> taskService.deleteById(TASK.getId()));
        verify(taskRepository).findById(TASK.getId());
        verify(taskRepository).delete(TASK);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(taskRepository.findById(TASK.getId())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteById(TASK.getId()));
        verify(taskRepository).findById(TASK.getId());
        verify(taskRepository, never()).delete(any());
    }

    @ParameterizedTest
    @MethodSource("provideFilters")
    void findWithFilters_ShouldReturnExpectedResults(TaskFilterDTO filter, List<Task> expectedTasks, long expectedTotal) {
        Page<Task> page = new PageImpl<>(expectedTasks);
        when(taskRepository.findWithFilters(filter.authorId(), filter.assigneeId(), filter.status(), filter.priority(), PAGEABLE)).thenReturn(page);

        Page<Task> result = taskService.findWithFilters(filter, PAGEABLE);

        assertEquals(expectedTotal, result.getTotalElements());
        verify(taskRepository).findWithFilters(filter.authorId(), filter.assigneeId(), filter.status(), filter.priority(), PAGEABLE);
    }

    static Stream<Arguments> provideFilters() {
        return Stream.of(
                Arguments.of(createTaskFilterDTO(null, null, null, null), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, null, null, null), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(null, ASSIGNEE_ID_EXAMPLE, null, null), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(null, null, TASK_STATUS_EXAMPLE_ENUM, null), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(null, null, null, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, ASSIGNEE_ID_EXAMPLE, null, null), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, null, TASK_STATUS_EXAMPLE_ENUM, null), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, null, null, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(null, ASSIGNEE_ID_EXAMPLE, TASK_STATUS_EXAMPLE_ENUM, null), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(null, ASSIGNEE_ID_EXAMPLE, null, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(null, null, TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, ASSIGNEE_ID_EXAMPLE, TASK_STATUS_EXAMPLE_ENUM, null), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, ASSIGNEE_ID_EXAMPLE, null, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, null, TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(null, ASSIGNEE_ID_EXAMPLE, TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(), TASKS, TASKS.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, ASSIGNEE_ID_EXAMPLE, TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), EMPTY_TASKS, EMPTY_TASKS.size())
        );
    }
}
