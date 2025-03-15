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
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;

import java.util.List;
import java.util.UUID;

@Tag(name = "User API", description = "API for managing users")
public interface UserApiSpec {

    @Operation(summary = "Get all users", description = "Returns a list of all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "No users found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<List<UserResponseDTO>> getAllUsers();

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "UUID of the user", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            UUID id
    );

    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<UserResponseDTO> createUser(@Valid UserRequestDTO userRequestDTO);
}
