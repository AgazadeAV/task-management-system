package ru.effectmobile.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.effectmobile.task_management_system.model.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email OR u.phoneNumber = :phoneNumber")
    Optional<User> findByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber);

    boolean existsByEmail(String email);
}
