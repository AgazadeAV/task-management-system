package ru.effectmobile.task_management_system.service;

import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface TaskManagementFacade {

    List<TaskResponseDTO> getAllTasks();

    TaskResponseDTO getTaskById(UUID id);

    TaskResponseDTO createTask(TaskRequestDTO taskDTO);

    TaskResponseDTO updateTask(UUID id, TaskRequestDTO taskDTO);

    void deleteTask(UUID id);

    List<CommentResponseDTO> getTaskComments(UUID taskId);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(UUID id);

    UserResponseDTO createUser(UserRequestDTO userDTO);

    CommentResponseDTO createComment(CommentRequestDTO commentDTO);
}
