package ru.effectmobile.task_management_system.service.base.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.exception.custom.notfound.TaskNotFoundException;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.repository.TaskRepository;
import ru.effectmobile.task_management_system.service.base.TaskService;

import java.util.UUID;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_NOT_FOUND_BY_ID_MESSAGE;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Task findById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, id)));
    }

    @Override
    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        Task task = findById(id);
        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> findWithFilters(TaskFilterDTO filter, Pageable pageable) {
        return taskRepository.findWithFilters(
                filter.authorId(),
                filter.assigneeId(),
                filter.status(),
                filter.priority(),
                pageable
        );
    }
}
