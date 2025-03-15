package ru.effectmobile.task_management_system.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.effectmobile.task_management_system.config.swagger.schemas.responses.CommentResponseDTOSchema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(implementation = CommentResponseDTOSchema.class)
public record CommentResponseDTO(
        UUID id,
        UUID taskId,
        String taskTitle,
        UUID authorId,
        String authorFullName,
        String text,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
