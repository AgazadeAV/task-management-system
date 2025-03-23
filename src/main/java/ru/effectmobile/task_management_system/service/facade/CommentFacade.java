package ru.effectmobile.task_management_system.service.facade;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;

import java.util.UUID;

public interface CommentFacade {

    Page<CommentResponseDTO> getTaskComments(UUID taskId, Pageable pageable);

    CommentResponseDTO createComment(@Valid CommentRequestDTO commentDTO, String email);

    void deleteComment(UUID id, String email);
}
