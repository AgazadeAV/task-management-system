package ru.effectmobile.task_management_system.swagger.specs;

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
import ru.effectmobile.task_management_system.dto.requests.TaskFilterDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;

import java.util.UUID;

import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_ID_EXAMPLE_JSON;

@Tag(name = "Task API", description = "API for managing tasks")
public interface TaskApiSpec {

    @Operation(summary = "Get all tasks", description = "Returns a paginated list of tasks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskResponseDTO.class)))
    })
    ResponseEntity<Page<TaskResponseDTO>> getAllTasks(Pageable pageable);

    @Operation(summary = "Get task by ID", description = "Retrieves a task by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<TaskResponseDTO> getTaskById(
            @Parameter(description = "UUID of the task", required = true, example = TASK_ID_EXAMPLE_JSON)
            UUID id
    );

    @Operation(summary = "Create a new task", description = "Creates a new task in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<TaskResponseDTO> createTask(@Valid TaskRequestDTO taskRequestDTO);

    @Operation(summary = "Update a task", description = "Updates an existing task by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<TaskResponseDTO> updateTask(
            @Parameter(description = "UUID of the task", required = true, example = TASK_ID_EXAMPLE_JSON)
            UUID id,
            @Valid TaskRequestDTO taskRequestDTO
    );

    @Operation(summary = "Delete a task", description = "Deletes a task by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Void> deleteTask(
            @Parameter(description = "UUID of the task", required = true, example = TASK_ID_EXAMPLE_JSON)
            UUID id
    );

    @Operation(summary = "Filter tasks", description = "Returns a paginated list of tasks matching the specified filters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "No tasks found matching the filters",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Invalid filter criteria",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Page<TaskResponseDTO>> getTasksWithFilters(
            @Valid TaskFilterDTO filter,
            Pageable pageable
    );
}
