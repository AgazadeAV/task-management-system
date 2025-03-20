package ru.effectmobile.task_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.exception.custom.auth.UserDoesntHaveEnoughRightsException;
import ru.effectmobile.task_management_system.exception.custom.notfound.CommentNotFoundException;
import ru.effectmobile.task_management_system.exception.custom.notfound.TaskNotFoundException;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.service.facade.CommentFacade;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.effectmobile.task_management_system.controller.CommentController.COMMENT_API_URL;
import static ru.effectmobile.task_management_system.controller.CommentController.CREATE_COMMENT;
import static ru.effectmobile.task_management_system.controller.CommentController.DELETE_COMMENT_BY_ID;
import static ru.effectmobile.task_management_system.controller.CommentController.GET_TASK_COMMENTS;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.COMMENT_DELETE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.COMMENT_NOT_FOUND_BY_ID_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_NOT_FOUND_BY_ID_MESSAGE;
import static ru.effectmobile.task_management_system.util.ModelCreator.createComment;
import static ru.effectmobile.task_management_system.util.ModelCreator.createCommentRequestDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createCommentResponseDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTask;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentFacade commentFacade;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    private final String apiPathPrefix;

    public CommentControllerTest(@Value("${api.base.url}") String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
        this.apiPathPrefix = this.apiBaseUrl + COMMENT_API_URL;
    }

    private static final Task TASK = createTask(TaskStatus.COMPLETED, TaskPriority.LOW);
    private static final Comment COMMENT = createComment();
    private static final CommentResponseDTO COMMENT_RESPONSE_DTO = createCommentResponseDTO();
    private static final Page<CommentResponseDTO> COMMENT_PAGE = new PageImpl<>(List.of(COMMENT_RESPONSE_DTO, COMMENT_RESPONSE_DTO));
    private static final CommentRequestDTO COMMENT_REQUEST_DTO = createCommentRequestDTO();

    @Test
    @WithMockUser
    void getTaskComments_Success() throws Exception {
        when(commentFacade.getTaskComments(any(UUID.class), any(Pageable.class))).thenReturn(COMMENT_PAGE);

        mockMvc.perform(get(apiPathPrefix + GET_TASK_COMMENTS, TASK.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getTaskComments_Unauthorized() throws Exception {
        mockMvc.perform(get(apiPathPrefix + GET_TASK_COMMENTS, TASK.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getTaskComments_TaskNotFound() throws Exception {
        when(commentFacade.getTaskComments(any(UUID.class), any(Pageable.class)))
                .thenThrow(new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, TASK.getId())));

        mockMvc.perform(get(apiPathPrefix + GET_TASK_COMMENTS, TASK.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createComment_Success() throws Exception {
        when(commentFacade.createComment(any(CommentRequestDTO.class), any(String.class)))
                .thenReturn(COMMENT_RESPONSE_DTO);

        mockMvc.perform(post(apiPathPrefix + CREATE_COMMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_REQUEST_DTO)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCommentRequests")
    @WithMockUser
    void createComment_BadRequest(CommentRequestDTO invalidRequest) throws Exception {
        mockMvc.perform(post(apiPathPrefix + CREATE_COMMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createComment_Unauthorized() throws Exception {
        mockMvc.perform(post(apiPathPrefix + CREATE_COMMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_REQUEST_DTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void createComment_TaskNotFound() throws Exception {
        when(commentFacade.createComment(any(CommentRequestDTO.class), any(String.class)))
                .thenThrow(new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, TASK.getId())));

        mockMvc.perform(post(apiPathPrefix + CREATE_COMMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(COMMENT_REQUEST_DTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteComment_Success() throws Exception {
        doNothing().when(commentFacade).deleteComment(any(UUID.class), any(String.class));

        mockMvc.perform(delete(apiPathPrefix + DELETE_COMMENT_BY_ID, COMMENT.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteComment_Unauthorized() throws Exception {
        mockMvc.perform(delete(apiPathPrefix + DELETE_COMMENT_BY_ID, COMMENT.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void deleteComment_Forbidden() throws Exception {
        doThrow(new UserDoesntHaveEnoughRightsException(COMMENT_DELETE_FORBIDDEN_MESSAGE)).when(commentFacade)
                .deleteComment(any(UUID.class), any(String.class));

        mockMvc.perform(delete(apiPathPrefix + DELETE_COMMENT_BY_ID, COMMENT.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void deleteComment_NotFound() throws Exception {
        doThrow(new CommentNotFoundException(String.format(COMMENT_NOT_FOUND_BY_ID_MESSAGE, COMMENT.getId()))).when(commentFacade)
                .deleteComment(any(UUID.class), any(String.class));

        mockMvc.perform(delete(apiPathPrefix + DELETE_COMMENT_BY_ID, COMMENT.getId()))
                .andExpect(status().isNotFound());
    }

    private static Stream<Arguments> provideInvalidCommentRequests() {
        return Stream.of(
                Arguments.of(new CommentRequestDTO(null, "Valid comment")),
                Arguments.of(new CommentRequestDTO(UUID.randomUUID(), "")),
                Arguments.of(new CommentRequestDTO(UUID.randomUUID(), "  ")),
                Arguments.of(new CommentRequestDTO(UUID.randomUUID(), "A".repeat(2001)))
        );
    }
}
