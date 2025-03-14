package ru.effectmobile.task_management_system.service.factory;

import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.model.MetaData;
import ru.effectmobile.task_management_system.model.Task;
import ru.effectmobile.task_management_system.model.User;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;

import static ru.effectmobile.task_management_system.service.mapper.EnumMapper.mapToEnum;

@Component
public class TaskFactory {

    public Task createTask(TaskRequestDTO dto, User author) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(mapToEnum(TaskStatus.class, dto.getStatus()))
                .priority(mapToEnum(TaskPriority.class, dto.getPriority()))
                .author(author)
                .metaData(new MetaData())
                .build();
    }
}
