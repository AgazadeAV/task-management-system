package ru.effectmobile.task_management_system.service;

import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.model.User;

public interface UserService extends AbstractService<User> {

    User findByEmail(String email);

    void validateExistingFields(UserRequestDTO request);
}
