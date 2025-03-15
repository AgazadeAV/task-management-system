package ru.effectmobile.task_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.exception.TaskNotFoundException;
import ru.effectmobile.task_management_system.model.Task;
import ru.effectmobile.task_management_system.repository.TaskRepository;
import ru.effectmobile.task_management_system.service.TaskService;

import java.util.List;
import java.util.UUID;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_NOT_FOUND_BY_ID_MESSAGE;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll();
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
}
