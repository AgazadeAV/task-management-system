package ru.effectmobile.task_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectmobile.task_management_system.dto.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.TaskResponseDTO;
import ru.effectmobile.task_management_system.service.TaskManagementFacade;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.base.url}" + TaskController.TASK_API_URI)
@RequiredArgsConstructor
public class TaskController {

    public static final String TASK_API_URI = "/tasks";
    public static final String GET_ALL_TASKS = "/tasks-list";
    public static final String GET_TASK_BY_ID = "/task/{id}";
    public static final String CREATE_TASK = "/create-task";
    public static final String UPDATE_TASK_BY_ID = "/update-task/{id}";
    public static final String DELETE_TASK_BY_ID = "/delete-task/{id}";

    private final TaskManagementFacade taskManagementFacade;

    @GetMapping(GET_ALL_TASKS)
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskManagementFacade.getAllTasks());
    }

    @GetMapping(GET_TASK_BY_ID)
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(taskManagementFacade.getTaskById(id));
    }

    @PostMapping(CREATE_TASK)
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.ok(taskManagementFacade.createTask(taskRequestDTO));
    }

    @PutMapping(UPDATE_TASK_BY_ID)
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable("id") UUID id, @Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        return ResponseEntity.ok(taskManagementFacade.updateTask(id, taskRequestDTO));
    }

    @DeleteMapping(DELETE_TASK_BY_ID)
    public ResponseEntity<Void> deleteTask(@PathVariable("id") UUID id) {
        taskManagementFacade.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
