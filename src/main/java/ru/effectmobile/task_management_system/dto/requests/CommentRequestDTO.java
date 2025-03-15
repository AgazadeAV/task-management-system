package ru.effectmobile.task_management_system.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.effectmobile.task_management_system.swagger.schemas.requests.CommentRequestDTOSchema;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(implementation = CommentRequestDTOSchema.class)
public class CommentRequestDTO {

    @NotNull(message = "Task ID cannot be null")
    private UUID taskId;

    @NotNull(message = "Author ID cannot be null")
    private UUID authorId;

    @NotBlank(message = "Comment text cannot be null")
    @Size(min = 1, max = 2000, message = "Comment text must be between 1 and 2000 characters")
    private String text;
}
