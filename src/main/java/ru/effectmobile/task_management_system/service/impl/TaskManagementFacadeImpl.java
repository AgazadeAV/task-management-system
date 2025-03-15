package ru.effectmobile.task_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.model.Comment;
import ru.effectmobile.task_management_system.model.Task;
import ru.effectmobile.task_management_system.model.User;
import ru.effectmobile.task_management_system.service.CommentService;
import ru.effectmobile.task_management_system.service.TaskManagementFacade;
import ru.effectmobile.task_management_system.service.TaskService;
import ru.effectmobile.task_management_system.service.UserService;
import ru.effectmobile.task_management_system.service.factory.CommentFactory;
import ru.effectmobile.task_management_system.service.factory.TaskFactory;
import ru.effectmobile.task_management_system.service.factory.UserFactory;
import ru.effectmobile.task_management_system.service.mapper.CommentMapper;
import ru.effectmobile.task_management_system.service.mapper.TaskMapper;
import ru.effectmobile.task_management_system.service.mapper.UserMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskManagementFacadeImpl implements TaskManagementFacade {

    private final TaskService taskService;
    private final UserService userService;
    private final CommentService commentService;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final TaskFactory taskFactory;
    private final UserFactory userFactory;
    private final CommentFactory commentFactory;

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getAllTasks() {
        return taskService.findAll()
                .stream()
                .map(taskMapper::taskToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(UUID id) {
        Task task = taskService.findById(id);
        return taskMapper.taskToResponseDTO(task);
    }

    @Override
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskDTO) {
        User author = userService.findById(taskDTO.getAuthorId());
        Task task = taskFactory.createTask(taskDTO, author);
        setAssignee(task, taskDTO.getAssigneeId());
        return taskMapper.taskToResponseDTO(taskService.save(task));
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(UUID id, TaskRequestDTO taskDTO) {
        Task task = taskService.findById(id);
        taskMapper.updateTaskFromRequestDTO(taskDTO, task);
        setAssignee(task, taskDTO.getAssigneeId());
        return taskMapper.taskToResponseDTO(taskService.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(UUID id) {
        taskService.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getTaskComments(UUID taskId) {
        return commentService.findByTaskId(taskId)
                .stream()
                .map(commentMapper::commentToResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userService.findAll()
                .stream()
                .map(userMapper::userToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(UUID id) {
        User user = userService.findById(id);
        return userMapper.userToResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        User user = userFactory.createUser(userDTO);
        return userMapper.userToResponseDTO(userService.save(user));
    }

    @Override
    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO commentDTO) {
        Task task = taskService.findById(commentDTO.getTaskId());
        User author = userService.findById(commentDTO.getAuthorId());
        Comment comment = commentFactory.createComment(commentDTO, task, author);
        return commentMapper.commentToResponseDTO(commentService.save(comment));
    }

    private void setAssignee(Task task, UUID assigneeId) {
        if (assigneeId == null) {
            task.setAssignee(null);
            return;
        }
        if (task.getAssignee() == null || !assigneeId.equals(task.getAssignee().getId())) {
            User assignee = userService.findById(assigneeId);
            task.setAssignee(assignee);
        }
    }

}
