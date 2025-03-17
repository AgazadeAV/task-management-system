package ru.effectmobile.task_management_system.service.base;

import ru.effectmobile.task_management_system.dto.filters.UserCredsExistanceCheckDTO;
import ru.effectmobile.task_management_system.model.entity.User;

public interface UserService extends BaseService<User> {

    User findByEmail(String email);

    void validateExistingFields(UserCredsExistanceCheckDTO request, UserCredsExistanceCheckDTO encryptedRequest);
}
