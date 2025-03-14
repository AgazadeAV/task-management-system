package ru.effectmobile.task_management_system.service.mapper;

import org.mapstruct.Mapper;
import ru.effectmobile.task_management_system.dto.CommentResponseDTO;
import ru.effectmobile.task_management_system.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponseDTO commentToResponseDTO(Comment comment);
}
