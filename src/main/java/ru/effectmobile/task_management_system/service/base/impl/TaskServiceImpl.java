package ru.effectmobile.task_management_system.service.base.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Task> findAll(Pageable pageable) {
        log.debug("Fetching all tasks with pagination: {}", pageable);
        return taskRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "tasksById", key = "#id")
    @Transactional(readOnly = true)
    public Task findById(UUID id) {
        log.debug("Searching for task with ID: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found with ID: {}", id);
                    return new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, id));
                });
    }

    @Override
    @CacheEvict(value = {"tasksById", "tasksByFilters"}, key = "#task.id")
    @Transactional
    public Task save(Task task) {
        log.info("Saving task: {}", task);
        return taskRepository.save(task);
    }

    @Override
    @CacheEvict(value = {"tasksById", "tasksByFilters"}, key = "#id")
    @Transactional
    public void deleteById(UUID id) {
        log.info("Deleting task with ID: {}", id);
        Task task = findById(id);
        taskRepository.delete(task);
        log.debug("Task deleted successfully: {}", id);
    }

    @Override
    @Cacheable(value = "tasksByFilters", key = "#filter.toString() + #pageable.toString()")
    @Transactional(readOnly = true)
    public Page<Task> findWithFilters(TaskFilterDTO filter, Pageable pageable) {
        log.debug("Fetching tasks with filters: {} and pagination: {}", filter, pageable);
        return taskRepository.findWithFilters(
                filter.authorId(),
                filter.assigneeId(),
                filter.status(),
                filter.priority(),
                pageable
        );
    }
}
