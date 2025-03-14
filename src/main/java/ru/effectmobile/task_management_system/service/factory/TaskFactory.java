package ru.effectmobile.task_management_system.service.factory;

import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.dto.TaskRequestDTO;
import ru.effectmobile.task_management_system.model.MetaData;
import ru.effectmobile.task_management_system.model.Task;
import ru.effectmobile.task_management_system.model.User;

@Component
public class TaskFactory {

    public Task createTask(TaskRequestDTO dto, User author) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .author(author)
                .metaData(new MetaData())
                .build();
    }
}
