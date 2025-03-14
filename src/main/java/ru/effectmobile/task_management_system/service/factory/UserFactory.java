package ru.effectmobile.task_management_system.service.factory;

import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.dto.UserRequestDTO;
import ru.effectmobile.task_management_system.model.MetaData;
import ru.effectmobile.task_management_system.model.User;

@Component
public class UserFactory {

    public User createUser(UserRequestDTO dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .metaData(new MetaData())
                .build();
    }
}
