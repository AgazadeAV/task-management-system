package ru.effectmobile.task_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.config.crypto.CipherService;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.exception.custom.conflict.EmailAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.custom.conflict.PhoneNumberAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.custom.conflict.UsernameAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.custom.notfound.UserNotFoundException;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.repository.UserRepository;
import ru.effectmobile.task_management_system.service.UserService;

import java.util.Optional;
import java.util.UUID;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.EMAIL_ALREADY_REGISTERED;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.PHONE_NUMBER_ALREADY_REGISTERED;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USERNAME_ALREADY_REGISTERED;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USER_NOT_FOUND_BY_EMAIL_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USER_NOT_FOUND_BY_ID_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CipherService cipherService;

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        log.debug("Fetching all users with pageable: {}", pageable);
        return userRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "usersById", key = "#id")
    @Transactional(readOnly = true)
    public User findById(UUID id) {
        log.debug("Searching for user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found by ID: {}", id);
                    return new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MESSAGE, id));
                });
    }

    @Override
    @CacheEvict(value = {"usersByEmail", "usersById"}, key = "#user.email")
    @Transactional
    public User save(User user) {
        log.info("Saving user: {}", cipherService.decrypt(user.getEmail()));
        return userRepository.save(user);
    }

    @Override
    @CacheEvict(value = {"usersByEmail", "usersById"}, key = "#id")
    @Transactional
    public void deleteById(UUID id) {
        log.info("Deleting user by ID: {}", id);
        User user = findById(id);
        userRepository.delete(user);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    @Cacheable(value = "usersByEmail", key = "#email")
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        log.debug("Searching for user by email: {}", cipherService.decrypt(email));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found by email: {}", cipherService.decrypt(email));
                    return new UserNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL_MESSAGE, cipherService.decrypt(email)));
                });
    }

    @Override
    public void validateExistingFields(UserRequestDTO request) {
        log.debug("Validating existing user fields: username={}, email={}, phone={}",
                request.username(), request.email(), request.phoneNumber());

        Optional<User> existingUser = userRepository.findByUsernameOrEmailOrPhoneNumber(
                request.username(),
                request.email(),
                request.phoneNumber()
        );

        existingUser.ifPresent(user -> {
            if (user.getUsername().equals(cipherService.encrypt(request.username()))) {
                log.warn("Username '{}' is already registered", request.username());
                throw new UsernameAlreadyRegisteredException(String.format(USERNAME_ALREADY_REGISTERED, request.username()));
            }
            if (user.getEmail().equals(cipherService.encrypt(request.email()))) {
                log.warn("Email '{}' is already registered", request.email());
                throw new EmailAlreadyRegisteredException(String.format(EMAIL_ALREADY_REGISTERED, request.email()));
            }
            if (user.getPhoneNumber().equals(cipherService.encrypt(request.phoneNumber()))) {
                log.warn("Phone number '{}' is already registered", request.phoneNumber());
                throw new PhoneNumberAlreadyRegisteredException(String.format(PHONE_NUMBER_ALREADY_REGISTERED, request.phoneNumber()));
            }
        });

        log.debug("User fields validation passed");
    }
}
