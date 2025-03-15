package ru.effectmobile.task_management_system.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.swagger.schemas.responses.UserResponseDTOSchema;

import java.util.UUID;

@Schema(implementation = UserResponseDTOSchema.class)
public record UserResponseDTO(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Role role
) {}
