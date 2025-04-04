package ru.effectmobile.task_management_system.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.effectmobile.task_management_system.config.swagger.specs.TaskApiSpec;
import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.service.facade.TaskFacade;

import java.security.Principal;
import java.util.UUID;

import static ru.effectmobile.task_management_system.service.mapper.EnumMapper.mapToEnumNullable;

@Slf4j
@RestController
@RequestMapping("${api.base.url}" + TaskController.TASK_API_URL)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TaskController implements TaskApiSpec {

    public static final String TASK_API_URL = "/tasks";
    public static final String GET_ALL_TASKS = "/tasks-list";
    public static final String GET_TASK_BY_ID = "/get-task/{id}";
    public static final String CREATE_TASK = "/create-task";
    public static final String UPDATE_TASK_BY_ID = "/update-task/{id}";
    public static final String DELETE_TASK_BY_ID = "/delete-task/{id}";
    public static final String GET_TASKS_WITH_FILTERS = "/filter";

    private final TaskFacade taskFacade;

    @GetMapping(GET_ALL_TASKS)
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(@ParameterObject Pageable pageable) {
        log.info("Fetching all tasks with pagination: {}", pageable);
        Page<TaskResponseDTO> response = taskFacade.getAllTasks(pageable);
        log.info("Retrieved {} tasks", response.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping(GET_TASK_BY_ID)
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable("id") UUID id) {
        log.info("Fetching task by ID '{}'", id);
        TaskResponseDTO response = taskFacade.getTaskById(id);
        log.info("Task retrieved: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping(CREATE_TASK)
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO, Principal principal) {
        log.info("Creating a new task: {}", taskRequestDTO);
        TaskResponseDTO response = taskFacade.createTask(taskRequestDTO, principal.getName());
        log.info("Task created successfully with ID '{}'", response.id());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(UPDATE_TASK_BY_ID)
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable("id") UUID id, @Valid @RequestBody TaskRequestDTO taskRequestDTO, Principal principal) {
        log.info("Updating task with ID '{}': {}", id, taskRequestDTO);
        TaskResponseDTO response = taskFacade.updateTask(id, taskRequestDTO, principal.getName());
        log.info("Task with ID '{}' updated successfully", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(DELETE_TASK_BY_ID)
    public ResponseEntity<Void> deleteTask(@PathVariable("id") UUID id, Principal principal) {
        log.info("Attempting to delete task with ID '{}'", id);
        taskFacade.deleteTask(id, principal.getName());
        log.info("Task with ID '{}' deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(GET_TASKS_WITH_FILTERS)
    public ResponseEntity<Page<TaskResponseDTO>> getTasksWithFilters(
            @RequestParam(required = false) UUID authorId,
            @RequestParam(required = false) UUID assigneeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @ParameterObject Pageable pageable
    ) {
        log.info("Fetching tasks with filters: authorId={}, assigneeId={}, status={}, priority={}, pagination={}",
                authorId, assigneeId, status, priority, pageable);
        TaskFilterDTO filter = new TaskFilterDTO(authorId, assigneeId, mapToEnumNullable(TaskStatus.class, status), mapToEnumNullable(TaskPriority.class, priority));
        Page<TaskResponseDTO> response = taskFacade.getTasksWithFilters(filter, pageable);
        log.info("Retrieved {} tasks matching the filters", response.getTotalElements());
        return ResponseEntity.ok(response);
    }
}
