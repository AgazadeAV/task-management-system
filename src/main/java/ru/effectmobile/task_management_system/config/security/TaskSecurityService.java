package ru.effectmobile.task_management_system.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.effectmobile.task_management_system.repository.TaskRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSecurityService {

    private final TaskRepository taskRepository;

    public boolean isTaskOwner(UUID taskId, String username) {
        log.debug("Checking if user '{}' is the owner of task '{}'", username, taskId);

        return taskRepository.findById(taskId)
                .map(task -> {
                    boolean isOwner = task.getAuthor().getEmail().equals(username);
                    log.debug("Task found. Owner check: {}", isOwner);
                    return isOwner;
                })
                .orElseGet(() -> {
                    log.warn("Task with ID '{}' not found.", taskId);
                    return false;
                });
    }
}
