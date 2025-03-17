package ru.effectmobile.task_management_system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author", target = "authorFullName", qualifiedByName = "getUserFullName")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "assignee", target = "assigneeFullName", qualifiedByName = "getUserFullNameOrNull")
    @Mapping(source = "metaData.createdAt", target = "createdAt")
    @Mapping(source = "metaData.updatedAt", target = "updatedAt")
    TaskResponseDTO taskToResponseDTO(Task task);

    @Mapping(target = "assignee", ignore = true)
    void updateTaskFromRequestDTO(TaskRequestDTO taskDTO, @MappingTarget Task task);

    @Named("getUserFullName")
    static String getUserFullName(User user) {
        return Stream.of(user.getFirstName(), user.getLastName())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

    @Named("getUserFullNameOrNull")
    static String getUserFullNameOrNull(User user) {
        return (user == null) ? null : getUserFullName(user);
    }
}
