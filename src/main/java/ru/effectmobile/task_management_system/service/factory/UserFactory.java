package ru.effectmobile.task_management_system.service.factory;

import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;

import static ru.effectmobile.task_management_system.service.mapper.EnumMapper.mapToEnum;

@Component
public class UserFactory {

    public User createUser(UserRequestDTO dto, MetaData metaData) {
        return User.builder()
                .username(dto.username())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(dto.password())
                .role(mapToEnum(Role.class, dto.role()))
                .birthDate(dto.birthDate())
                .phoneNumber(dto.phoneNumber())
                .metaData(metaData)
                .build();
    }
}
