package ru.effectmobile.task_management_system.service.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;

import static ru.effectmobile.task_management_system.service.mapper.EnumMapper.mapToEnum;

@Slf4j
@Component
public class TaskFactory {

    public Task createTask(TaskRequestDTO dto, User author, MetaData metaData) {
        log.debug("Creating Task with title: {}, author: {}, status: {}, priority: {}",
                dto.title(), author.getEmail(), dto.status(), dto.priority());

        Task task = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .status(mapToEnum(TaskStatus.class, dto.status()))
                .priority(mapToEnum(TaskPriority.class, dto.priority()))
                .author(author)
                .metaData(metaData)
                .build();

        log.debug("Task created successfully: {}", task);
        return task;
    }
}
