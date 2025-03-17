package ru.effectmobile.task_management_system.dto.filters;


public record UserCredsExistanceCheckDTO (
        String username,
        String email,
        String phoneNumber
) {}
