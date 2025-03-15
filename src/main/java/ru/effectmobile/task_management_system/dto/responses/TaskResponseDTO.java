package ru.effectmobile.task_management_system.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.config.swagger.schemas.responses.TaskResponseDTOSchema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(implementation = TaskResponseDTOSchema.class)
public record TaskResponseDTO(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        UUID authorId,
        String authorFullName,
        UUID assigneeId,
        String assigneeFullName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
