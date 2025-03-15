package ru.effectmobile.task_management_system.dto.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.config.swagger.schemas.requests.TaskFilterDTOSchema;

import java.util.UUID;

@Schema(implementation = TaskFilterDTOSchema.class)
public record TaskFilterDTO(
        UUID authorId,
        UUID assigneeId,
        TaskStatus status,
        TaskPriority priority
) {}
