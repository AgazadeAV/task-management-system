package ru.effectmobile.task_management_system.service.mapper;

import org.mapstruct.Mapper;
import ru.effectmobile.task_management_system.dto.UserResponseDTO;
import ru.effectmobile.task_management_system.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO userToResponseDTO(User user);
}
