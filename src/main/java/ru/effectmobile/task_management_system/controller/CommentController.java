package ru.effectmobile.task_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.service.TaskManagementFacade;
import ru.effectmobile.task_management_system.swagger.specs.CommentApiSpec;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.base.url}" + CommentController.COMMENT_API_URI)
@RequiredArgsConstructor
public class CommentController implements CommentApiSpec {

    public static final String COMMENT_API_URI = "/comments";
    public static final String GET_TASK_COMMENTS = "/task/{taskId}";
    public static final String CREATE_COMMENT = "/create-comment";

    private final TaskManagementFacade taskManagementFacade;

    @GetMapping(GET_TASK_COMMENTS)
    public ResponseEntity<List<CommentResponseDTO>> getTaskComments(@PathVariable("taskId") UUID taskId) {
        List<CommentResponseDTO> comments = taskManagementFacade.getTaskComments(taskId);
        return CollectionUtils.isEmpty(comments) ? ResponseEntity.noContent().build() : ResponseEntity.ok(comments);
    }

    @PostMapping(CREATE_COMMENT)
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO createdComment = taskManagementFacade.createComment(commentRequestDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
}
