package ru.effectmobile.task_management_system.swagger.specs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import ru.effectmobile.task_management_system.dto.requests.LoginRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.AuthResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;

@Tag(name = "Authentication API", description = "API for user authentication")
public interface AuthApiSpec {

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid email or password",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<AuthResponseDTO> login(@Valid LoginRequestDTO request);

    @Operation(summary = "User registration", description = "Registers a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Email, phone number or username already in use",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<UserResponseDTO> register(@Valid UserRequestDTO request);
}
