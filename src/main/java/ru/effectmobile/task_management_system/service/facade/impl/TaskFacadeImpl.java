package ru.effectmobile.task_management_system.service.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.exception.custom.auth.UserDoesntHaveEnoughRightsException;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.service.base.TaskService;
import ru.effectmobile.task_management_system.service.base.UserService;
import ru.effectmobile.task_management_system.service.facade.TaskFacade;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.factory.TaskFactory;
import ru.effectmobile.task_management_system.service.mapper.TaskMapper;

import java.util.UUID;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_CREATE_OR_DELETE_FORBIDDEN_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.TASK_UPDATE_FORBIDDEN_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskFacadeImpl implements TaskFacade {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskFactory taskFactory;
    private final UserService userService;
    private final MetaDataFactory metaDataFactory;

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        log.debug("Fetching all tasks with pagination: {}", pageable);
        Page<TaskResponseDTO> tasks = taskService.findAll(pageable)
                .map(taskMapper::taskToResponseDTO);
        log.debug("Found {} tasks", tasks.getTotalElements());
        return tasks;
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(UUID id) {
        log.debug("Fetching task by ID: {}", id);
        Task task = taskService.findById(id);
        log.debug("Task found: {}", task);
        return taskMapper.taskToResponseDTO(task);
    }

    @Override
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskDTO, String email) {
        User author = userService.findByEmail(email);
        canUserCreateOrDeleteTask(email);
        log.info("Creating a new task with title: {}", taskDTO.title());
        MetaData metaData = metaDataFactory.createMetaData();
        Task task = taskFactory.createTask(taskDTO, author, metaData);
        setAssignee(task, taskDTO.assigneeId());

        Task savedTask = taskService.save(task);
        log.info("Task created successfully with ID: {}", savedTask.getId());

        return taskMapper.taskToResponseDTO(savedTask);
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(UUID id, TaskRequestDTO taskDTO, String email) {
        canUserUpdateTask(email, taskDTO);
        log.info("Updating task with ID: {}", id);
        Task task = taskService.findById(id);
        taskMapper.updateTaskFromRequestDTO(taskDTO, task);
        setAssignee(task, taskDTO.assigneeId());

        Task savedTask = taskService.save(task);
        log.info("Task updated successfully: {}", savedTask);

        return taskMapper.taskToResponseDTO(savedTask);
    }

    @Override
    @Transactional
    public void deleteTask(UUID id, String email) {
        canUserCreateOrDeleteTask(email);
        log.warn("Deleting task with ID: {}", id);
        taskService.deleteById(id);
        log.info("Task deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> getTasksWithFilters(TaskFilterDTO filter, Pageable pageable) {
        log.debug("Fetching tasks with filters: {}, pagination: {}", filter, pageable);
        Page<TaskResponseDTO> tasks = taskService.findWithFilters(filter, pageable)
                .map(taskMapper::taskToResponseDTO);
        log.debug("Found {} tasks with applied filters", tasks.getTotalElements());
        return tasks;
    }

    private void setAssignee(Task task, UUID assigneeId) {
        if (assigneeId == null) {
            log.debug("Removing assignee from task ID: {}", task.getId());
            task.setAssignee(null);
            return;
        }
        if (task.getAssignee() == null || !assigneeId.equals(task.getAssignee().getId())) {
            log.debug("Assigning user ID: {} to task ID: {}", assigneeId, task.getId());
            User assignee = userService.findById(assigneeId);
            task.setAssignee(assignee);
        }
    }

    private void canUserUpdateTask(String email, TaskRequestDTO taskDTO) {
        User user = userService.findByEmail(email);
        Role role = user.getRole();
        if (!role.equals(Role.ROLE_ADMIN)) {
            if (!user.getId().equals(taskDTO.assigneeId())) {
                throw new UserDoesntHaveEnoughRightsException(TASK_UPDATE_FORBIDDEN_MESSAGE);
            }
        }
    }

    private void canUserCreateOrDeleteTask(String email) {
        Role role = userService.findByEmail(email).getRole();
        if (!role.equals(Role.ROLE_ADMIN)) {
            throw new UserDoesntHaveEnoughRightsException(TASK_CREATE_OR_DELETE_FORBIDDEN_MESSAGE);
        }
    }
}
