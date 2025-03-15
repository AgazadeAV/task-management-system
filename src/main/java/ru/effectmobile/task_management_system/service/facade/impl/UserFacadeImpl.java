package ru.effectmobile.task_management_system.service.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.requests.LoginRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.AuthResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.exception.PasswordDoesNotMatchException;
import ru.effectmobile.task_management_system.model.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.service.JwtService;
import ru.effectmobile.task_management_system.service.UserService;
import ru.effectmobile.task_management_system.service.facade.UserFacade;
import ru.effectmobile.task_management_system.service.factory.UserFactory;
import ru.effectmobile.task_management_system.service.impl.AesService;
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
    private final AesService aesService;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userService.findAll(pageable)
                .map(user -> userMapper.userToResponseDTO(user, aesService));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(UUID id) {
        User user = userService.findById(id);
        return userMapper.userToResponseDTO(user, aesService);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        User user = userFactory.createUser(userDTO);
        handleSensitiveData(user);
        return userMapper.userToResponseDTO(userService.save(user), aesService);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO loginDTO) {
        String email = loginDTO.email();
        String password = loginDTO.password();
        User user = userService.findByEmail(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordDoesNotMatchException(PASSWORD_DOESNT_MATCH_MESSAGE);
        }

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token);
    }

    @Override
    @Transactional
    public UserResponseDTO register(UserRequestDTO request) {
        userService.validateExistingFields(request);
        User user = userFactory.createUser(request);
        user.setRole(Role.USER);
        handleSensitiveData(user);
        userService.save(user);
        return userMapper.userToResponseDTO(user, aesService);
    }

    void handleSensitiveData(User user) {
        Optional.ofNullable(user.getPassword())
                .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
        String encryptedUsername = aesService.encrypt(user.getUsername());
        String encryptedEmail = aesService.encrypt(user.getEmail());
        String encryptedPhoneNumber = aesService.encrypt(user.getPhoneNumber());
        user.setUsername(encryptedUsername);
        user.setEmail(encryptedEmail);
        user.setPhoneNumber(encryptedPhoneNumber);
    }
}
