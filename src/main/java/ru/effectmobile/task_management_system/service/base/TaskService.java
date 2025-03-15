package ru.effectmobile.task_management_system.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.model.entity.Task;

public interface TaskService extends BaseService<Task> {

    Page<Task> findWithFilters(TaskFilterDTO filter, Pageable pageable);
}
