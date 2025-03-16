package ru.effectmobile.task_management_system.config.swagger.schemas.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.BIRTH_DATE_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.EMAIL_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.FIRST_NAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.LAST_NAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.PASSWORD_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.PHONE_NUMBER_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ROLE_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.USERNAME_EXAMPLE;

@Getter
@Builder
public final class UserRequestDTOSchema {

    @Schema(example = USERNAME_EXAMPLE, description = "Username of the user.")
    private final String username;

    @Schema(example = FIRST_NAME_EXAMPLE, description = "First name of the user.")
    private final String firstName;

    @Schema(example = LAST_NAME_EXAMPLE, description = "Last name of the user.")
    private final String lastName;

    @Schema(example = EMAIL_EXAMPLE, description = "Email address of the user.")
    private final String email;

    @Schema(example = PASSWORD_EXAMPLE, description = "Password for the user account.")
    private final String password;

    @Schema(example = ROLE_EXAMPLE, description = "Role assigned to the user.")
    private final String role;

    @Schema(example = BIRTH_DATE_EXAMPLE, description = "User's birth date.")
    private final String birthDate;

    @Schema(example = PHONE_NUMBER_EXAMPLE, description = "User's phone number.")
    private final String phoneNumber;
}
