package ru.effectmobile.task_management_system.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.effectmobile.task_management_system.dto.filters.UserCredsExistanceCheckDTO;
import ru.effectmobile.task_management_system.exception.custom.conflict.EmailAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.custom.conflict.PhoneNumberAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.custom.conflict.UsernameAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.custom.notfound.UserNotFoundException;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.repository.UserRepository;
import ru.effectmobile.task_management_system.service.base.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUserCredsExistanceCheckDTO;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private static final User USER = createUser(Role.ROLE_ADMIN);
    private static final PageRequest PAGEABLE = PageRequest.of(0, 10);
    private static final String ENCRYPTED_USERNAME = "encryptedUsername";
    private static final String ENCRYPTED_EMAIL = "encryptedRequestEmail";
    private static final String ENCRYPTED_PHONE_NUMBER = "123456789";

    @Test
    void findAll_ShouldReturnPageOfUsers() {
        Page<User> page = new PageImpl<>(List.of(USER));
        when(userRepository.findAll(PAGEABLE)).thenReturn(page);

        Page<User> result = userService.findAll(PAGEABLE);

        assertEquals(1, result.getTotalElements());
        verify(userRepository).findAll(PAGEABLE);
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() {
        when(userRepository.findById(USER.getId())).thenReturn(Optional.of(USER));

        User result = userService.findById(USER.getId());

        assertEquals(USER.getId(), result.getId());
        verify(userRepository).findById(USER.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(USER.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(USER.getId()));
        verify(userRepository).findById(USER.getId());
    }

    @Test
    void save_ShouldReturnSavedUser() {
        when(userRepository.save(USER)).thenReturn(USER);

        User result = userService.save(USER);

        assertEquals(USER, result);
        verify(userRepository).save(USER);
    }

    @Test
    void deleteById_ShouldDeleteUser_WhenExists() {
        when(userRepository.findById(USER.getId())).thenReturn(Optional.of(USER));
        doNothing().when(userRepository).delete(USER);

        assertDoesNotThrow(() -> userService.deleteById(USER.getId()));
        verify(userRepository).findById(USER.getId());
        verify(userRepository).delete(USER);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(USER.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(USER.getId()));
        verify(userRepository).findById(USER.getId());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));

        User result = userService.findByEmail(USER.getEmail());

        assertEquals(USER.getEmail(), result.getEmail());
        verify(userRepository).findByEmail(USER.getEmail());
    }

    @Test
    void findByEmail_ShouldThrowException_WhenNotFound() {
        when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(USER.getEmail()));
        verify(userRepository).findByEmail(USER.getEmail());
    }

    @Test
    void validateExistingFields_ShouldThrowException_WhenUsernameExists() {
        UserCredsExistanceCheckDTO request = createUserCredsExistanceCheckDTO();
        UserCredsExistanceCheckDTO encryptedRequest = new UserCredsExistanceCheckDTO(request.username(), ENCRYPTED_EMAIL, ENCRYPTED_PHONE_NUMBER);

        when(userRepository.findByUsernameOrEmailOrPhoneNumber(
                encryptedRequest.username(),
                encryptedRequest.email(),
                encryptedRequest.phoneNumber()
        )).thenReturn(Optional.of(USER));

        assertThrows(UsernameAlreadyRegisteredException.class, () -> userService.validateExistingFields(request, encryptedRequest));
        verify(userRepository).findByUsernameOrEmailOrPhoneNumber(any(), any(), any());
    }

    @Test
    void validateExistingFields_ShouldThrowException_WhenEmailExists() {
        UserCredsExistanceCheckDTO request = createUserCredsExistanceCheckDTO();
        UserCredsExistanceCheckDTO encryptedRequest = new UserCredsExistanceCheckDTO(ENCRYPTED_USERNAME, request.email(), ENCRYPTED_PHONE_NUMBER);

        when(userRepository.findByUsernameOrEmailOrPhoneNumber(
                encryptedRequest.username(),
                encryptedRequest.email(),
                encryptedRequest.phoneNumber()
        )).thenReturn(Optional.of(USER));

        assertThrows(EmailAlreadyRegisteredException.class, () -> userService.validateExistingFields(request, encryptedRequest));
    }

    @Test
    void validateExistingFields_ShouldThrowException_WhenPhoneNumberExists() {
        UserCredsExistanceCheckDTO request = createUserCredsExistanceCheckDTO();
        UserCredsExistanceCheckDTO encryptedRequest = new UserCredsExistanceCheckDTO(ENCRYPTED_USERNAME, ENCRYPTED_EMAIL, request.phoneNumber());

        when(userRepository.findByUsernameOrEmailOrPhoneNumber(
                encryptedRequest.username(),
                encryptedRequest.email(),
                encryptedRequest.phoneNumber()
        )).thenReturn(Optional.of(USER));

        assertThrows(PhoneNumberAlreadyRegisteredException.class, () -> userService.validateExistingFields(request, encryptedRequest));
    }

    @Test
    void validateExistingFields_ShouldPass_WhenNoConflicts() {
        UserCredsExistanceCheckDTO request = createUserCredsExistanceCheckDTO();
        UserCredsExistanceCheckDTO encryptedRequest = createUserCredsExistanceCheckDTO();

        when(userRepository.findByUsernameOrEmailOrPhoneNumber(
                encryptedRequest.username(),
                encryptedRequest.email(),
                encryptedRequest.phoneNumber()
        )).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.validateExistingFields(request, encryptedRequest));
        verify(userRepository).findByUsernameOrEmailOrPhoneNumber(any(), any(), any());
    }
}
