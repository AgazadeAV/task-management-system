package ru.effectmobile.task_management_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.dto.requests.TaskFilterDTO;
import ru.effectmobile.task_management_system.model.Task;

public interface TaskService extends AbstractService<Task> {

    Page<Task> findWithFilters(TaskFilterDTO filter, Pageable pageable);
}
