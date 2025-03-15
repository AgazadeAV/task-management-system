package ru.effectmobile.task_management_system.config.swagger.schemas.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.TOKEN_EXAMPLE;

@Getter
public class AuthResponseDTOSchema {

    @Schema(example = TOKEN_EXAMPLE, description = "JWT token used for authenticated requests.")
    private String token;
}
