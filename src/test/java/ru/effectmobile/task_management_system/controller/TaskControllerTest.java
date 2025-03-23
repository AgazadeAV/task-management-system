package ru.effectmobile.task_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.exception.custom.auth.UserDoesntHaveEnoughRightsException;
import ru.effectmobile.task_management_system.exception.custom.notfound.TaskNotFoundException;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.service.facade.TaskFacade;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.effectmobile.task_management_system.controller.TaskController.CREATE_TASK;
import static ru.effectmobile.task_management_system.controller.TaskController.DELETE_TASK_BY_ID;
import static ru.effectmobile.task_management_system.controller.TaskController.GET_ALL_TASKS;
import static ru.effectmobile.task_management_system.controller.TaskController.GET_TASKS_WITH_FILTERS;
import static ru.effectmobile.task_management_system.controller.TaskController.GET_TASK_BY_ID;
import static ru.effectmobile.task_management_system.controller.TaskController.TASK_API_URL;
import static ru.effectmobile.task_management_system.controller.TaskController.UPDATE_TASK_BY_ID;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_CREATE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_DELETE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_NOT_FOUND_BY_ID_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_UPDATE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTask;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTaskRequestDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTaskResponseDTO;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskFacade taskFacade;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    private String apiPathPrefix;

    private static final Task TASK = createTask(TaskStatus.COMPLETED, TaskPriority.LOW);
    private static final TaskResponseDTO TASK_RESPONSE_DTO = createTaskResponseDTO();
    private static final Page<TaskResponseDTO> TASKS = new PageImpl<>(List.of(TASK_RESPONSE_DTO, TASK_RESPONSE_DTO));
    private static final TaskRequestDTO TASK_REQUEST_DTO = createTaskRequestDTO();

    @BeforeEach
    void setUp() {
        this.apiPathPrefix = apiBaseUrl + TASK_API_URL;
    }

    @Test
    @WithMockUser
    void getAllTasks_WithAuthorizedRoles_ReturnsOk() throws Exception {
        when(taskFacade.getAllTasks(any(Pageable.class))).thenReturn(TASKS);
        mockMvc.perform(get(apiPathPrefix + GET_ALL_TASKS))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTasks_Unauthorized() throws Exception {
        mockMvc.perform(get(apiPathPrefix + GET_ALL_TASKS))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getTaskById_Success() throws Exception {
        when(taskFacade.getTaskById(any(UUID.class))).thenReturn(TASK_RESPONSE_DTO);
        mockMvc.perform(get(apiPathPrefix + GET_TASK_BY_ID, TASK.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getTaskById_Unauthorized() throws Exception {
        mockMvc.perform(get(apiPathPrefix + GET_TASK_BY_ID, TASK.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getTaskById_NotFound() throws Exception {
        when(taskFacade.getTaskById(any(UUID.class)))
                .thenThrow(new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, TASK.getId())));
        mockMvc.perform(get(apiPathPrefix + GET_TASK_BY_ID, TASK.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createTask_Success() throws Exception {
        when(taskFacade.createTask(any(TaskRequestDTO.class), any(String.class))).thenReturn(TASK_RESPONSE_DTO);
        mockMvc.perform(post(apiPathPrefix + CREATE_TASK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TASK_REQUEST_DTO)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("ru.effectmobile.task_management_system.util.ModelCreator#provideInvalidTaskRequests")
    @WithMockUser
    void createTask_BadRequest(TaskRequestDTO invalidRequests) throws Exception {
        mockMvc.perform(post(apiPathPrefix + CREATE_TASK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequests)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTask_Unauthorized() throws Exception {
        mockMvc.perform(post(apiPathPrefix + CREATE_TASK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TASK_REQUEST_DTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void createTask_Forbidden() throws Exception {
        when(taskFacade.createTask(any(TaskRequestDTO.class), any(String.class)))
                .thenThrow(new UserDoesntHaveEnoughRightsException(TASK_CREATE_FORBIDDEN_MESSAGE));
        mockMvc.perform(post(apiPathPrefix + CREATE_TASK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TASK_REQUEST_DTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void updateTask_Success() throws Exception {
        when(taskFacade.updateTask(any(UUID.class), any(TaskRequestDTO.class), any(String.class)))
                .thenReturn(TASK_RESPONSE_DTO);

        mockMvc.perform(put(apiPathPrefix + UPDATE_TASK_BY_ID, TASK.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TASK_REQUEST_DTO)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("ru.effectmobile.task_management_system.util.ModelCreator#provideInvalidTaskRequests")
    @WithMockUser
    void updateTask_BadRequest(TaskRequestDTO invalidRequests) throws Exception {
        mockMvc.perform(put(apiPathPrefix + UPDATE_TASK_BY_ID, TASK.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequests)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask_Unauthorized() throws Exception {
        mockMvc.perform(put(apiPathPrefix + UPDATE_TASK_BY_ID, TASK.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void updateTask_Forbidden() throws Exception {
        when(taskFacade.updateTask(any(UUID.class), any(TaskRequestDTO.class), any(String.class)))
                .thenThrow(new UserDoesntHaveEnoughRightsException(TASK_UPDATE_FORBIDDEN_MESSAGE));

        mockMvc.perform(put(apiPathPrefix + UPDATE_TASK_BY_ID, TASK.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TASK_REQUEST_DTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void updateTask_NotFound() throws Exception {
        when(taskFacade.updateTask(any(UUID.class), any(TaskRequestDTO.class), any(String.class)))
                .thenThrow(new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, TASK.getId())));

        mockMvc.perform(put(apiPathPrefix + UPDATE_TASK_BY_ID, TASK.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TASK_REQUEST_DTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteTask_Success() throws Exception {
        doNothing().when(taskFacade).deleteTask(any(UUID.class), any(String.class));
        mockMvc.perform(delete(apiPathPrefix + DELETE_TASK_BY_ID, TASK.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_Unauthorized() throws Exception {
        mockMvc.perform(delete(apiPathPrefix + DELETE_TASK_BY_ID, TASK.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void deleteTask_Forbidden() throws Exception {
        doThrow(new UserDoesntHaveEnoughRightsException(TASK_DELETE_FORBIDDEN_MESSAGE))
                .when(taskFacade).deleteTask(any(UUID.class), any(String.class));
        mockMvc.perform(delete(apiPathPrefix + DELETE_TASK_BY_ID, TASK.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void deleteTask_NotFound() throws Exception {
        doThrow(new TaskNotFoundException(String.format(TASK_NOT_FOUND_BY_ID_MESSAGE, TASK.getId())))
                .when(taskFacade).deleteTask(any(UUID.class), any(String.class));

        mockMvc.perform(delete(apiPathPrefix + DELETE_TASK_BY_ID, TASK.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getTasksWithFilters_Success() throws Exception {
        when(taskFacade.getTasksWithFilters(any(TaskFilterDTO.class), any(Pageable.class)))
                .thenReturn(TASKS);

        mockMvc.perform(get(apiPathPrefix + GET_TASKS_WITH_FILTERS)
                        .param("status", "IN_PROGRESS")
                        .param("priority", "HIGH")
                        .param("authorId", UUID.randomUUID().toString())
                        .param("assigneeId", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getTasksWithFilters_BadRequest() throws Exception {
        mockMvc.perform(get(apiPathPrefix + GET_TASKS_WITH_FILTERS)
                        .param("status", "INVALID_STATUS")
                        .param("priority", "INVALID_PRIORITY"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTasksWithFilters_Unauthorized() throws Exception {
        mockMvc.perform(get(apiPathPrefix + GET_TASKS_WITH_FILTERS))
                .andExpect(status().isUnauthorized());
    }
}
