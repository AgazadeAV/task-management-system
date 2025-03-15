package ru.effectmobile.task_management_system.service.factory;

import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.model.MetaData;
import ru.effectmobile.task_management_system.model.User;
import ru.effectmobile.task_management_system.model.enums.Role;

import static ru.effectmobile.task_management_system.service.mapper.EnumMapper.mapToEnum;

@Component
public class UserFactory {

    public User createUser(UserRequestDTO dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(mapToEnum(Role.class, dto.getRole()))
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .metaData(new MetaData())
                .build();
    }
}
