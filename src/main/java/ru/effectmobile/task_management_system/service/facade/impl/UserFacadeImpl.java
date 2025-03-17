package ru.effectmobile.task_management_system.service.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.UserCredsExistanceCheckDTO;
import ru.effectmobile.task_management_system.dto.requests.LoginRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.AuthResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.exception.custom.auth.PasswordDoesNotMatchException;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.service.base.CipherService;
import ru.effectmobile.task_management_system.service.base.JwtService;
import ru.effectmobile.task_management_system.service.base.UserService;
import ru.effectmobile.task_management_system.service.facade.UserFacade;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.factory.UserFactory;
import ru.effectmobile.task_management_system.service.mapper.UserMapper;

import java.util.Optional;
import java.util.UUID;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.PASSWORD_DOESNT_MATCH_MESSAGE;

@Service
@RequiredArgsConstructor
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
        return userService.findAll(pageable)
                .map(user -> userMapper.userToResponseDTO(user, cipherService));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(UUID id) {
        User user = userService.findById(id);
        return userMapper.userToResponseDTO(user, cipherService);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {
        validateExistingFields(request);
        MetaData metaData = metaDataFactory.createMetaData();
        User user = userFactory.createUser(request, metaData);
        handleSensitiveData(user, request);
        User savedUser = userService.save(user);
        return userMapper.userToResponseDTO(savedUser, cipherService);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO loginDTO) {
        String email = loginDTO.email();
        String password = loginDTO.password();
        String encryptedEmail = cipherService.encrypt(email);
        User user = userService.findByEmail(encryptedEmail);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordDoesNotMatchException(PASSWORD_DOESNT_MATCH_MESSAGE);
        }

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token);
    }

    @Override
    @Transactional
    public UserResponseDTO register(UserRequestDTO request) {
        validateExistingFields(request);
        MetaData metaData = metaDataFactory.createMetaData();
        User user = userFactory.createUser(request, metaData);
        user.setRole(Role.USER);
        handleSensitiveData(user, request);
        userService.save(user);
        return userMapper.userToResponseDTO(user, cipherService);
    }

    void handleSensitiveData(User user, UserRequestDTO request) {
        Optional.ofNullable(request.password())
                .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
        String encryptedUsername = cipherService.encrypt(request.username());
        String encryptedEmail = cipherService.encrypt(request.email());
        String encryptedPhoneNumber = cipherService.encrypt(request.phoneNumber());
        user.setUsername(encryptedUsername);
        user.setEmail(encryptedEmail);
        user.setPhoneNumber(encryptedPhoneNumber);
    }

    private void validateExistingFields(UserRequestDTO request) {
        UserCredsExistanceCheckDTO requestForCheck =
                new UserCredsExistanceCheckDTO(
                                request.username(),
                                request.email(),
                                request.phoneNumber());
        UserCredsExistanceCheckDTO encryptedRequestForCheck =
                new UserCredsExistanceCheckDTO(
                        cipherService.encrypt(request.username()),
                        cipherService.encrypt(request.email()),
                        cipherService.encrypt(request.phoneNumber()));
        userService.validateExistingFields(requestForCheck, encryptedRequestForCheck);
    }
}
