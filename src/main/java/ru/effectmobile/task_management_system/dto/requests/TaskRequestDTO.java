package ru.effectmobile.task_management_system.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.effectmobile.task_management_system.config.swagger.schemas.requests.TaskRequestDTOSchema;

import java.util.UUID;

@Schema(implementation = TaskRequestDTOSchema.class)
public record TaskRequestDTO(

        @NotBlank(message = "Title cannot be blank")
        @Size(max = 255, message = "Title cannot exceed 255 characters")
        String title,

        @NotBlank(message = "Description cannot be blank")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @NotNull(message = "Task status cannot be null")
        String status,

        @NotNull(message = "Task priority cannot be null")
        String priority,

        @NotNull(message = "Author ID cannot be null")
        UUID authorId,

        UUID assigneeId
) {}
