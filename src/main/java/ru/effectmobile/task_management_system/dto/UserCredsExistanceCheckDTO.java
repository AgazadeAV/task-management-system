package ru.effectmobile.task_management_system.dto;


public record UserCredsExistanceCheckDTO (
        String username,
        String email,
        String phoneNumber
) {}
