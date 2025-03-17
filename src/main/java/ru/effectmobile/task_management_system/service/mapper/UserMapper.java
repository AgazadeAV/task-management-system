package ru.effectmobile.task_management_system.service.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.service.base.CipherService;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", source = "username", qualifiedByName = "decryptData")
    @Mapping(target = "email", source = "email", qualifiedByName = "decryptData")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "decryptData")
    UserResponseDTO userToResponseDTO(User user, @Context CipherService aesService);

    @Named("decryptData")
    static String decryptData(String data, @Context CipherService aesService) {
        return aesService.decrypt(data);
    }
}
