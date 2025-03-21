package ru.effectmobile.task_management_system.service.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUserRequestDTO;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CipherService cipherService;

    private static final User USER = createUser(Role.ROLE_ADMIN);
    private static final PageRequest PAGEABLE = PageRequest.of(0, 10);

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
        when(cipherService.decrypt(USER.getEmail())).thenReturn(USER.getEmail());
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
        when(cipherService.decrypt(USER.getEmail())).thenReturn(USER.getEmail());

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
    void validateExistingFields_ShouldPassValidation_WhenNoConflicts() {
        UserRequestDTO request = createUserRequestDTO();

        when(userRepository.findByUsernameOrEmailOrPhoneNumber(
                request.username(), request.email(), request.phoneNumber()))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.validateExistingFields(request));
    }

    private static Stream<Arguments> provideExistingUsersForValidation() {
        UserRequestDTO request = createUserRequestDTO();

        User userWithSameUsername = new User();
        userWithSameUsername.setUsername(request.username());
        userWithSameUsername.setEmail("other@example.com");
        userWithSameUsername.setPhoneNumber("+70000000000");

        User userWithSameEmail = new User();
        userWithSameEmail.setUsername("other_user");
        userWithSameEmail.setEmail(request.email());
        userWithSameEmail.setPhoneNumber("+70000000000");

        User userWithSamePhone = new User();
        userWithSamePhone.setUsername("other_user");
        userWithSamePhone.setEmail("other@example.com");
        userWithSamePhone.setPhoneNumber(request.phoneNumber());

        return Stream.of(
                Arguments.of(userWithSameUsername, UsernameAlreadyRegisteredException.class),
                Arguments.of(userWithSameEmail, EmailAlreadyRegisteredException.class),
                Arguments.of(userWithSamePhone, PhoneNumberAlreadyRegisteredException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("provideExistingUsersForValidation")
    void validateExistingFields_ShouldThrowExpectedException(User existingUser, Class<? extends RuntimeException> expectedException) {
        UserRequestDTO request = createUserRequestDTO();

        when(userRepository.findByUsernameOrEmailOrPhoneNumber(
                request.username(), request.email(), request.phoneNumber()))
                .thenReturn(Optional.of(existingUser));

        when(cipherService.decrypt(request.username())).thenReturn(request.username());
        when(cipherService.decrypt(request.email())).thenReturn(request.email());
        when(cipherService.decrypt(request.phoneNumber())).thenReturn(request.phoneNumber());

        assertThrows(expectedException, () -> userService.validateExistingFields(request));
    }
}
