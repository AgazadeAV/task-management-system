package ru.effectmobile.task_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private UUID id;
    private TaskResponseDTO taskDTO;
    private UserResponseDTO userDTO;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
