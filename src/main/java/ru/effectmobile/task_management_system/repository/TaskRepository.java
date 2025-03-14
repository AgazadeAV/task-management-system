package ru.effectmobile.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectmobile.task_management_system.model.Task;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByAuthorId(UUID authorId);

    List<Task> findByAssigneeId(UUID assigneeId);
}
