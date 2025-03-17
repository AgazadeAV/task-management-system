package ru.effectmobile.task_management_system.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.effectmobile.task_management_system.config.swagger.schemas.responses.AuthResponseDTOSchema;

@Schema(implementation = AuthResponseDTOSchema.class)
public record AuthResponseDTO(
        String token
) {}
