package ru.effectmobile.task_management_system.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.effectmobile.task_management_system.config.crypto.CipherService;
import ru.effectmobile.task_management_system.config.security.UserDetailsServiceImpl;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.util.DefaultInputs.EMAIL_EXAMPLE;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUser;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private CipherService cipherService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private static final User USER = createUser(Role.ROLE_ADMIN);

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        when(cipherService.decrypt(EMAIL_EXAMPLE)).thenReturn(EMAIL_EXAMPLE);
        when(userService.findByEmail(EMAIL_EXAMPLE)).thenReturn(USER);

        UserDetails userDetails = userDetailsService.loadUserByUsername(EMAIL_EXAMPLE);

        assertNotNull(userDetails);
        assertEquals(USER.getEmail(), userDetails.getUsername());
        assertEquals(USER.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(USER.getRole().name())));

        verify(userService).findByEmail(EMAIL_EXAMPLE);
        verify(cipherService, atLeastOnce()).decrypt(EMAIL_EXAMPLE);
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(cipherService.decrypt(EMAIL_EXAMPLE)).thenReturn(EMAIL_EXAMPLE);
        when(userService.findByEmail(EMAIL_EXAMPLE)).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(EMAIL_EXAMPLE));

        assertTrue(exception.getMessage().contains(EMAIL_EXAMPLE));
        verify(userService).findByEmail(EMAIL_EXAMPLE);
        verify(cipherService, atLeastOnce()).decrypt(EMAIL_EXAMPLE);
    }
}
