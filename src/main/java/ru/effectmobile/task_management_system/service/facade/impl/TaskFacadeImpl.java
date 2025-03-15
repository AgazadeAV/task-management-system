package ru.effectmobile.task_management_system.service.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.service.base.TaskService;
import ru.effectmobile.task_management_system.service.base.UserService;
import ru.effectmobile.task_management_system.service.facade.TaskFacade;
import ru.effectmobile.task_management_system.service.factory.TaskFactory;
import ru.effectmobile.task_management_system.service.mapper.TaskMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskFacadeImpl implements TaskFacade {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskFactory taskFactory;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        return taskService.findAll(pageable)
                .map(taskMapper::taskToResponseDTO);
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
        User author = userService.findById(taskDTO.authorId());
        Task task = taskFactory.createTask(taskDTO, author);
        setAssignee(task, taskDTO.assigneeId());
        return taskMapper.taskToResponseDTO(taskService.save(task));
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(UUID id, TaskRequestDTO taskDTO) {
        Task task = taskService.findById(id);
        taskMapper.updateTaskFromRequestDTO(taskDTO, task);
        setAssignee(task, taskDTO.assigneeId());
        return taskMapper.taskToResponseDTO(taskService.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(UUID id) {
        taskService.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> getTasksWithFilters(TaskFilterDTO filter, Pageable pageable) {
        return taskService.findWithFilters(filter, pageable)
                .map(taskMapper::taskToResponseDTO);
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
