package ru.effectmobile.task_management_system.swagger.schemas.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import static ru.effectmobile.task_management_system.util.DefaultInputs.*;

@Getter
public class UserResponseDTOSchema {

    @Schema(example = USER_ID_EXAMPLE_JSON, description = "Unique identifier of the user.")
    private String id;

    @Schema(example = USERNAME_EXAMPLE, description = "Username of the user.")
    private String username;

    @Schema(example = FIRST_NAME_EXAMPLE, description = "First name of the user.")
    private String firstName;

    @Schema(example = LAST_NAME_EXAMPLE, description = "Last name of the user.")
    private String lastName;

    @Schema(example = EMAIL_EXAMPLE, description = "Email of the user.")
    private String email;

    @Schema(example = ROLE_EXAMPLE, description = "Role of the user.")
    private String role;
}
