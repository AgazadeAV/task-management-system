package ru.effectmobile.task_management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    @Email
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String phoneNumber;
}
