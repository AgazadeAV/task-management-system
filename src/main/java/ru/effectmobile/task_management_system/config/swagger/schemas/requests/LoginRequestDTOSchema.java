package ru.effectmobile.task_management_system.config.swagger.schemas.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.EMAIL_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.PASSWORD_EXAMPLE;

@Getter
@Builder
public final class LoginRequestDTOSchema {

    @Schema(example = EMAIL_EXAMPLE, description = "User's email address used for authentication.")
    private final String email;

    @Schema(example = PASSWORD_EXAMPLE, description = "User's password.")
    private final String password;
}
