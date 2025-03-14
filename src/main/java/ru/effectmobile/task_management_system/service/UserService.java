package ru.effectmobile.task_management_system.service;

import ru.effectmobile.task_management_system.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<User> findAll();

    User findById(UUID id);

    User save(User user);

    void deleteById(UUID id);
}
