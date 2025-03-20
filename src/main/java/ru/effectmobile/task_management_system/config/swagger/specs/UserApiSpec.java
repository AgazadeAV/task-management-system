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
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;

import java.util.UUID;

import static ru.effectmobile.task_management_system.util.DefaultInputs.USER_ID_EXAMPLE_JSON;

@Tag(name = "User API", description = "API for managing users")
public interface UserApiSpec {

    @Operation(summary = "Get all users", description = "Returns a paginated list of all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission to get user list",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable);

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission to get user",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "UUID of the user", required = true, example = USER_ID_EXAMPLE_JSON)
            UUID id
    );

    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden - user does not have permission to create new user",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<UserResponseDTO> createUser(@Valid UserRequestDTO userRequestDTO);
}
