package ru.effectmobile.task_management_system.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.effectmobile.task_management_system.repository.TaskRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskSecurityService {

    private final TaskRepository taskRepository;

    public boolean isTaskOwner(UUID taskId, String username) {
        return taskRepository.findById(taskId)
                .map(task -> task.getAuthor().getEmail().equals(username))
                .orElse(false);
    }
}
