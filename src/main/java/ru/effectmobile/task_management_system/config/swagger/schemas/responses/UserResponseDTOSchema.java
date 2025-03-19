package ru.effectmobile.task_management_system.config.swagger.schemas.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.EMAIL_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.FIRST_NAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.LAST_NAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.PHONE_NUMBER_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ROLE_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.USERNAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.USER_ID_EXAMPLE_JSON;

@Getter
@Builder
public final class UserResponseDTOSchema {

    @Schema(example = USER_ID_EXAMPLE_JSON, description = "Unique identifier of the user.")
    private final String id;

    @Schema(example = USERNAME_EXAMPLE, description = "Username of the user.")
    private final String username;

    @Schema(example = FIRST_NAME_EXAMPLE, description = "First name of the user.")
    private final String firstName;

    @Schema(example = LAST_NAME_EXAMPLE, description = "Last name of the user.")
    private final String lastName;

    @Schema(example = EMAIL_EXAMPLE, description = "Email of the user.")
    private final String email;

    @Schema(example = PHONE_NUMBER_EXAMPLE, description = "Phone number of the user.")
    private final String phoneNumber;

    @Schema(example = ROLE_EXAMPLE, description = "Role of the user.")
    private final String role;
}
