package ru.effectmobile.task_management_system.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.effectmobile.task_management_system.config.swagger.schemas.requests.LoginRequestDTOSchema;
import ru.effectmobile.task_management_system.service.validation.annotation.Password;

@Schema(implementation = LoginRequestDTOSchema.class)
public record LoginRequestDTO(

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @Password
        @Size(min = 8, max = 22, message = "Password must be between 8 and 22 characters long")
        String password
) {}
