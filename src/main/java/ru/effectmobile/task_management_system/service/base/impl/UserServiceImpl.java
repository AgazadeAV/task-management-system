package ru.effectmobile.task_management_system.service.base.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.exception.EmailAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.PhoneNumberAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.UserNotFoundException;
import ru.effectmobile.task_management_system.exception.UsernameAlreadyRegisteredException;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.repository.UserRepository;
import ru.effectmobile.task_management_system.service.base.UserService;

import java.util.UUID;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.EMAIL_ALREADY_REGISTERED;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.PHONE_NUMBER_ALREADY_REGISTERED;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USERNAME_ALREADY_REGISTERED;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USER_NOT_FOUND_BY_EMAIL_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USER_NOT_FOUND_BY_ID_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MESSAGE, id)));
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL_MESSAGE, email)));
    }

    @Override
    public void validateExistingFields(UserRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException(String.format(EMAIL_ALREADY_REGISTERED, request.email()));
        }
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new PhoneNumberAlreadyRegisteredException(String.format(PHONE_NUMBER_ALREADY_REGISTERED, request.phoneNumber()));
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyRegisteredException(String.format(USERNAME_ALREADY_REGISTERED, request.username()));
        }
    }
}
