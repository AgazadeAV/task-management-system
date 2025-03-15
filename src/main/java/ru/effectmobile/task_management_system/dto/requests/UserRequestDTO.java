package ru.effectmobile.task_management_system.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.effectmobile.task_management_system.config.swagger.schemas.requests.UserRequestDTOSchema;
import ru.effectmobile.task_management_system.service.validation.annotation.Password;

import java.time.LocalDate;

@Schema(implementation = UserRequestDTOSchema.class)
public record UserRequestDTO(

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
        String username,

        @NotBlank(message = "First name cannot be blank")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters long")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters long")
        String lastName,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @Password
        @Size(min = 8, max = 22, message = "Password must be between 8 and 22 characters long")
        String password,

        @NotNull(message = "Role cannot be null")
        String role,

        @NotNull(message = "Birth date cannot be null")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(regexp = "\\+7\\d{10}", message = "Phone number must start with +7 and contain exactly 10 digits")
        String phoneNumber
) {}
