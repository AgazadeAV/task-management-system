package ru.effectmobile.task_management_system.service.base.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectmobile.task_management_system.dto.filters.UserCredsExistanceCheckDTO;
import ru.effectmobile.task_management_system.exception.custom.conflict.EmailAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.custom.conflict.PhoneNumberAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.custom.conflict.UsernameAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.custom.notfound.UserNotFoundException;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.repository.UserRepository;
import ru.effectmobile.task_management_system.service.base.UserService;

import java.util.Optional;
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
    public void validateExistingFields(UserCredsExistanceCheckDTO request, UserCredsExistanceCheckDTO encryptedRequest) {
        Optional<User> existingUser = userRepository.findByUsernameOrEmailOrPhoneNumber(
                encryptedRequest.username(),
                encryptedRequest.email(),
                encryptedRequest.phoneNumber()
        );

        existingUser.ifPresent(user -> {
            if (user.getUsername().equals(encryptedRequest.username())) {
                throw new UsernameAlreadyRegisteredException(String.format(USERNAME_ALREADY_REGISTERED, request.username()));
            }
            if (user.getEmail().equals(encryptedRequest.email())) {
                throw new EmailAlreadyRegisteredException(String.format(EMAIL_ALREADY_REGISTERED, request.email()));
            }
            if (user.getPhoneNumber().equals(encryptedRequest.phoneNumber())) {
                throw new PhoneNumberAlreadyRegisteredException(String.format(PHONE_NUMBER_ALREADY_REGISTERED, request.phoneNumber()));
            }
        });
    }

}
