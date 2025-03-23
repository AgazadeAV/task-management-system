package ru.effectmobile.task_management_system.service.facade;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;

import java.util.UUID;

public interface TaskFacade {

    Page<TaskResponseDTO> getAllTasks(Pageable pageable);

    TaskResponseDTO getTaskById(UUID id);

    TaskResponseDTO createTask(@Valid TaskRequestDTO taskDTO, String email);

    TaskResponseDTO updateTask(UUID id, @Valid TaskRequestDTO taskDTO, String email);

    void deleteTask(UUID id, String email);

    Page<TaskResponseDTO> getTasksWithFilters(TaskFilterDTO filter, Pageable pageable);
}
