package ru.effectmobile.task_management_system.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.swagger.schemas.responses.UserResponseDTOSchema;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(implementation = UserResponseDTOSchema.class)
public class UserResponseDTO {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
