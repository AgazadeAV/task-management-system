package ru.effectmobile.task_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.service.facade.TaskFacade;
import ru.effectmobile.task_management_system.config.swagger.specs.TaskApiSpec;

import java.util.UUID;

@RestController
@RequestMapping("${api.base.url}" + TaskController.TASK_API_URI)
@RequiredArgsConstructor
public class TaskController implements TaskApiSpec {

    public static final String TASK_API_URI = "/tasks";
    public static final String GET_ALL_TASKS = "/tasks-list";
    public static final String GET_TASK_BY_ID = "/task/{id}";
    public static final String CREATE_TASK = "/create-task";
    public static final String UPDATE_TASK_BY_ID = "/update-task/{id}";
    public static final String DELETE_TASK_BY_ID = "/delete-task/{id}";
    public static final String GET_TASKS_WITH_FILTERS = "/filter";

    private final TaskFacade taskFacade;

    @GetMapping(GET_ALL_TASKS)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(taskFacade.getAllTasks(pageable));
    }

    @GetMapping(GET_TASK_BY_ID)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(taskFacade.getTaskById(id));
    }

    @PostMapping(CREATE_TASK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.ok(taskFacade.createTask(taskRequestDTO));
    }

    @PutMapping(UPDATE_TASK_BY_ID)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable("id") UUID id, @Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.ok(taskFacade.updateTask(id, taskRequestDTO));
    }

    @DeleteMapping(DELETE_TASK_BY_ID)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") UUID id) {
        taskFacade.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(GET_TASKS_WITH_FILTERS)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksWithFilters(@RequestBody TaskFilterDTO filter, @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(taskFacade.getTasksWithFilters(filter, pageable));
    }
}
