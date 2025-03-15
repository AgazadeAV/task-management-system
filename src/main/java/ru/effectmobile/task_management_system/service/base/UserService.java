package ru.effectmobile.task_management_system.service.base;

import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.model.entity.User;

public interface UserService extends BaseService<User> {

    User findByEmail(String email);

    void validateExistingFields(UserRequestDTO request);
}
