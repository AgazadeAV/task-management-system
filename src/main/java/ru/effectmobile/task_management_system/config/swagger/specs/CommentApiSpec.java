package ru.effectmobile.task_management_system.config.swagger.specs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;

import java.security.Principal;
import java.util.UUID;

import static ru.effectmobile.task_management_system.util.DefaultInputs.COMMENT_ID_EXAMPLE_JSON;

@Tag(name = "Comment API", description = "API for managing comments on tasks")
public interface CommentApiSpec {

    @Operation(summary = "Get comments for a task", description = "Returns a paginated list of comments for a task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Page<CommentResponseDTO>> getTaskComments(
            @Parameter(description = "UUID of the task", required = true, example = COMMENT_ID_EXAMPLE_JSON)
            UUID taskId,
            Pageable pageable
    );

    @Operation(summary = "Create a new comment", description = "Adds a new comment to a task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully",
                    content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<CommentResponseDTO> createComment(@Valid CommentRequestDTO commentRequestDTO, Principal principal);

    @Operation(summary = "Delete a comment", description = "Deletes a comment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission to delete this comment",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Void> deleteComment(
            @Parameter(description = "UUID of the comment", required = true, example = COMMENT_ID_EXAMPLE_JSON)
            UUID id, Principal principal
    );
}
