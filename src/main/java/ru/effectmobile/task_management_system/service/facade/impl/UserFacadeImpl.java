package ru.effectmobile.task_management_system.service.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.effectmobile.task_management_system.config.crypto.CipherService;
import ru.effectmobile.task_management_system.config.security.JwtService;
import ru.effectmobile.task_management_system.dto.requests.LoginRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.AuthResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.exception.custom.auth.PasswordDoesNotMatchException;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.service.base.UserService;
import ru.effectmobile.task_management_system.service.facade.UserFacade;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.factory.UserFactory;
import ru.effectmobile.task_management_system.service.mapper.UserMapper;

import java.util.Optional;
import java.util.UUID;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.PASSWORD_DOESNT_MATCH_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserFactory userFactory;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CipherService cipherService;
    private final MetaDataFactory metaDataFactory;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination: {}", pageable);
        Page<UserResponseDTO> users = userService.findAll(pageable)
                .map(user -> userMapper.userToResponseDTO(user, cipherService));
        log.debug("Found {} users", users.getTotalElements());
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(UUID id) {
        log.debug("Fetching user by ID: {}", id);
        User user = userService.findById(id);
        log.debug("User found: {}", user);
        return userMapper.userToResponseDTO(user, cipherService);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {
        log.info("Creating a new user with username: {}", request.username());
        userService.validateExistingFields(request);
        MetaData metaData = metaDataFactory.createMetaData();
        User user = userFactory.createUser(request, metaData);
        handleSensitiveData(user, request);
        User savedUser = userService.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return userMapper.userToResponseDTO(savedUser, cipherService);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO loginDTO) {
        log.info("User attempting login: {}", loginDTO.email());
        String email = loginDTO.email();
        String password = loginDTO.password();
        String encryptedEmail = cipherService.encrypt(email);
        User user = userService.findByEmail(encryptedEmail);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Login failed for user: {}", email);
            throw new PasswordDoesNotMatchException(PASSWORD_DOESNT_MATCH_MESSAGE);
        }

        String token = jwtService.generateToken(user);
        log.info("User logged in successfully: {}", email);
        return new AuthResponseDTO(token);
    }

    @Override
    @Transactional
    public UserResponseDTO register(UserRequestDTO request) {
        log.info("Registering new user with email: {}", request.email());
        userService.validateExistingFields(request);
        MetaData metaData = metaDataFactory.createMetaData();
        User user = userFactory.createUser(request, metaData);
        user.setRole(Role.ROLE_USER);
        handleSensitiveData(user, request);
        userService.save(user);
        log.info("User registered successfully with ID: {}", user.getId());
        return userMapper.userToResponseDTO(user, cipherService);
    }

    private void handleSensitiveData(User user, UserRequestDTO request) {
        log.debug("Handling sensitive data encryption for user: {}", request.username());
        Optional.ofNullable(request.password())
                .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
        user.setUsername(cipherService.encrypt(request.username()));
        user.setEmail(cipherService.encrypt(request.email()));
        user.setPhoneNumber(cipherService.encrypt(request.phoneNumber()));
    }
}
