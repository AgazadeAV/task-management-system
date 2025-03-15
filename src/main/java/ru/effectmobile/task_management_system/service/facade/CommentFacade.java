package ru.effectmobile.task_management_system.service.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;

import java.util.UUID;

public interface CommentFacade {

    Page<CommentResponseDTO> getTaskComments(UUID taskId, Pageable pageable);

    CommentResponseDTO createComment(CommentRequestDTO commentDTO);

    void deleteComment(UUID id);
}
