package ru.effectmobile.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectmobile.task_management_system.model.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
