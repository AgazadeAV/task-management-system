package ru.effectmobile.task_management_system.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.User;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "task.title", target = "taskTitle")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author", target = "authorFullName", qualifiedByName = "getUserFullName")
    @Mapping(source = "metaData.createdAt", target = "createdAt")
    @Mapping(source = "metaData.updatedAt", target = "updatedAt")
    CommentResponseDTO commentToResponseDTO(Comment comment);

    @Named("getUserFullName")
    static String getUserFullName(User user) {
        return Stream.of(user.getFirstName(), user.getLastName())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }
}
