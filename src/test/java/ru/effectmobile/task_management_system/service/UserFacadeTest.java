package ru.effectmobile.task_management_system.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import ru.effectmobile.task_management_system.service.facade.impl.UserFacadeImpl;
import ru.effectmobile.task_management_system.service.factory.MetaDataFactory;
import ru.effectmobile.task_management_system.service.factory.UserFactory;
import ru.effectmobile.task_management_system.service.mapper.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.util.DefaultInputs.EMAIL_EXAMPLE;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAuthResponseDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createLoginRequestDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createMetaData;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUserRequestDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUserResponseDTO;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserFactory userFactory;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private CipherService cipherService;

    @Mock
    private MetaDataFactory metaDataFactory;

    @InjectMocks
    private UserFacadeImpl userFacade;

    private static final User USER = createUser(Role.ROLE_ADMIN);
    private static final UserRequestDTO REQUEST_DTO = createUserRequestDTO();
    private static final UserResponseDTO RESPONSE_DTO = createUserResponseDTO();
    private static final LoginRequestDTO LOGIN_REQUEST_DTO = createLoginRequestDTO();
    private static final AuthResponseDTO AUTH_RESPONSE_DTO = createAuthResponseDTO();
    private static final MetaData META_DATA = createMetaData();

    @Test
    void getAllUsers_ShouldReturnUsers() {
        Pageable pageable = mock(Pageable.class);
        Page<User> userPage = new PageImpl<>(List.of(USER));
        when(userService.findAll(pageable)).thenReturn(userPage);
        when(userMapper.userToResponseDTO(USER, cipherService)).thenReturn(RESPONSE_DTO);

        Page<UserResponseDTO> result = userFacade.getAllUsers(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(RESPONSE_DTO, result.getContent().get(0));
        verify(userService).findAll(pageable);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userService.findById(USER.getId())).thenReturn(USER);
        when(userMapper.userToResponseDTO(USER, cipherService)).thenReturn(RESPONSE_DTO);

        UserResponseDTO result = userFacade.getUserById(USER.getId());

        assertEquals(RESPONSE_DTO, result);
        verify(userService).findById(USER.getId());
    }

    @Test
    void createUser_ShouldReturnSavedUser() {
        when(metaDataFactory.createMetaData()).thenReturn(META_DATA);
        when(userFactory.createUser(REQUEST_DTO, META_DATA)).thenReturn(USER);
        when(userService.save(USER)).thenReturn(USER);
        when(userMapper.userToResponseDTO(USER, cipherService)).thenReturn(RESPONSE_DTO);

        UserResponseDTO result = userFacade.createUser(REQUEST_DTO);

        assertEquals(RESPONSE_DTO, result);
        verify(userService).save(USER);
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreCorrect() {
        when(cipherService.encrypt(LOGIN_REQUEST_DTO.email())).thenReturn(EMAIL_EXAMPLE);
        when(userService.findByEmail(cipherService.encrypt(REQUEST_DTO.email()))).thenReturn(USER);
        when(passwordEncoder.matches(LOGIN_REQUEST_DTO.password(), USER.getPassword())).thenReturn(true);
        when(jwtService.generateToken(USER)).thenReturn(AUTH_RESPONSE_DTO.token());

        AuthResponseDTO result = userFacade.login(LOGIN_REQUEST_DTO);

        assertEquals(AUTH_RESPONSE_DTO.token(), result.token());
        verify(jwtService).generateToken(USER);
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsIncorrect() {
        when(cipherService.encrypt(LOGIN_REQUEST_DTO.email())).thenReturn(EMAIL_EXAMPLE);
        when(userService.findByEmail(cipherService.encrypt(REQUEST_DTO.email()))).thenReturn(USER);
        when(passwordEncoder.matches(LOGIN_REQUEST_DTO.password(), USER.getPassword())).thenReturn(false);

        assertThrows(PasswordDoesNotMatchException.class, () -> userFacade.login(LOGIN_REQUEST_DTO));
        verify(userService).findByEmail(EMAIL_EXAMPLE);
    }

    @Test
    void register_ShouldReturnRegisteredUser() {
        when(metaDataFactory.createMetaData()).thenReturn(META_DATA);
        when(userFactory.createUser(REQUEST_DTO, META_DATA)).thenReturn(USER);
        when(userService.save(USER)).thenReturn(USER);
        when(userMapper.userToResponseDTO(USER, cipherService)).thenReturn(RESPONSE_DTO);

        UserResponseDTO result = userFacade.register(REQUEST_DTO);

        assertEquals(RESPONSE_DTO, result);
        verify(userService).save(USER);
    }
}
