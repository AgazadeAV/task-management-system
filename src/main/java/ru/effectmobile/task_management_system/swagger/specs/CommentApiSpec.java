package ru.effectmobile.task_management_system.swagger.specs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import ru.effectmobile.task_management_system.dto.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.CommentResponseDTO;

import java.util.List;
import java.util.UUID;

@Tag(name = "Comment API", description = "API for managing comments on tasks")
public interface CommentApiSpec {

    @Operation(
            summary = "Get comments for a task",
            description = "Returns a list of comments associated with a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "204", description = "Comments for this task not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<List<CommentResponseDTO>> getTaskComments(
            @Parameter(description = "UUID of the task", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            UUID taskId
    );

    @Operation(
            summary = "Create a new comment",
            description = "Adds a new comment to a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully",
                    content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Task or user not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<CommentResponseDTO> createComment(@Valid CommentRequestDTO commentRequestDTO);
}
