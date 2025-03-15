package ru.effectmobile.task_management_system.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.effectmobile.task_management_system.config.swagger.schemas.requests.CommentRequestDTOSchema;

import java.util.UUID;

@Schema(implementation = CommentRequestDTOSchema.class)
public record CommentRequestDTO(

        @NotNull(message = "Task ID cannot be null")
        UUID taskId,

        @NotNull(message = "Author ID cannot be null")
        UUID authorId,

        @NotBlank(message = "Comment text cannot be null")
        @Size(min = 1, max = 2000, message = "Comment text must be between 1 and 2000 characters")
        String text
) {}
