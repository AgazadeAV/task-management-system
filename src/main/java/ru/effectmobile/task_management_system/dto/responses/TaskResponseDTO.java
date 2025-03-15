package ru.effectmobile.task_management_system.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UUID authorId;
    private String authorFullName;
    private UUID assigneeId;
    private String assigneeFullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
