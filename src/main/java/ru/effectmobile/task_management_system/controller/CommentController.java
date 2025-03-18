package ru.effectmobile.task_management_system.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectmobile.task_management_system.config.swagger.specs.CommentApiSpec;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.service.facade.CommentFacade;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${api.base.url}" + CommentController.COMMENT_API_URI)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CommentController implements CommentApiSpec {

    public static final String COMMENT_API_URI = "/comments";
    public static final String GET_TASK_COMMENTS = "/task/{taskId}";
    public static final String CREATE_COMMENT = "/create-comment";
    public static final String DELETE_COMMENT_BY_ID = "/delete-comment/{id}";

    private final CommentFacade commentFacade;

    @GetMapping(GET_TASK_COMMENTS)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Page<CommentResponseDTO>> getTaskComments(@PathVariable("taskId") UUID taskId, @ParameterObject Pageable pageable) {
        log.info("Fetching comments for task ID '{}'", taskId);
        Page<CommentResponseDTO> response = commentFacade.getTaskComments(taskId, pageable);
        log.info("Retrieved {} comments for task ID '{}'", response.getTotalElements(), taskId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(CREATE_COMMENT)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        log.info("Creating a new comment for task ID '{}'", commentRequestDTO.taskId());
        CommentResponseDTO response = commentFacade.createComment(commentRequestDTO);
        log.info("Comment created successfully with ID '{}'", response.id());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(DELETE_COMMENT_BY_ID)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @commentSecurityService.isCommentOwner(#id, authentication.principal.username)")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") UUID id) {
        log.info("Attempting to delete comment with ID '{}'", id);
        commentFacade.deleteComment(id);
        log.info("Comment with ID '{}' deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
