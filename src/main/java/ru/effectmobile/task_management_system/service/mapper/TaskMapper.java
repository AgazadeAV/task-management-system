package ru.effectmobile.task_management_system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.effectmobile.task_management_system.dto.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.TaskResponseDTO;
import ru.effectmobile.task_management_system.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponseDTO taskToResponseDTO(Task task);

    void updateTaskFromRequestDTO(TaskRequestDTO taskDTO, @MappingTarget Task task);
}
