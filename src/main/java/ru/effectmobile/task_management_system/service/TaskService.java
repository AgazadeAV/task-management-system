package ru.effectmobile.task_management_system.service;

import ru.effectmobile.task_management_system.model.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    List<Task> findAll();

    Task findById(UUID id);

    Task save(Task task);

    void deleteById(UUID id);
}
