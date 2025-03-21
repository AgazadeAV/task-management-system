package ru.effectmobile.task_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.effectmobile.task_management_system.dto.requests.LoginRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.AuthResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.exception.custom.conflict.UsernameAlreadyRegisteredException;
import ru.effectmobile.task_management_system.exception.custom.notfound.UserNotFoundException;
import ru.effectmobile.task_management_system.service.facade.UserFacade;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.effectmobile.task_management_system.controller.AuthController.AUTH_API_URL;
import static ru.effectmobile.task_management_system.controller.AuthController.LOGIN;
import static ru.effectmobile.task_management_system.controller.AuthController.REGISTER;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.EMAIL_ALREADY_REGISTERED;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USER_NOT_FOUND_BY_EMAIL_MESSAGE;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAuthResponseDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createLoginRequestDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUserRequestDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUserResponseDTO;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserFacade userFacade;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    private String apiPathPrefix;

    private static final AuthResponseDTO AUTH_RESPONSE_DTO = createAuthResponseDTO();
    private static final UserResponseDTO USER_RESPONSE_DTO = createUserResponseDTO();
    private static final LoginRequestDTO LOGIN_REQUEST_DTO = createLoginRequestDTO();
    private static final UserRequestDTO USER_REQUEST_DTO = createUserRequestDTO();

    @BeforeEach
    void setUp() {
        this.apiPathPrefix = apiBaseUrl + AUTH_API_URL;
    }

    @Test
    void login_Success() throws Exception {
        when(userFacade.login(any(LoginRequestDTO.class))).thenReturn(AUTH_RESPONSE_DTO);

        mockMvc.perform(post(apiPathPrefix + LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(LOGIN_REQUEST_DTO)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLoginRequests")
    void login_BadRequest(LoginRequestDTO invalidRequest) throws Exception {
        mockMvc.perform(post(apiPathPrefix + LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_UserNotFound() throws Exception {
        when(userFacade.login(any(LoginRequestDTO.class)))
                .thenThrow(new UserNotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL_MESSAGE, LOGIN_REQUEST_DTO.email())));

        mockMvc.perform(post(apiPathPrefix + LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(LOGIN_REQUEST_DTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void register_Success() throws Exception {
        when(userFacade.register(any(UserRequestDTO.class))).thenReturn(USER_RESPONSE_DTO);

        mockMvc.perform(post(apiPathPrefix + REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(USER_REQUEST_DTO)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserRequests")
    void register_BadRequest(UserRequestDTO invalidRequest) throws Exception {
        mockMvc.perform(post(apiPathPrefix + REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_Conflict() throws Exception {
        when(userFacade.register(any(UserRequestDTO.class)))
                .thenThrow(new UsernameAlreadyRegisteredException(String.format(EMAIL_ALREADY_REGISTERED, USER_REQUEST_DTO.email())));

        mockMvc.perform(post(apiPathPrefix + REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(USER_REQUEST_DTO)))
                .andExpect(status().isConflict());
    }

    private static Stream<Arguments> provideInvalidLoginRequests() {
        return Stream.of(
                Arguments.of(new LoginRequestDTO("", "ValidP@ssw0rd")),
                Arguments.of(new LoginRequestDTO("not-an-email", "ValidP@ssw0rd")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", null)),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "short1!")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "long".repeat(6) + "1!")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "password1!")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "PASSWORD1!")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "Password!")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "Password1"))

        );
    }

    private static Stream<Arguments> provideInvalidUserRequests() {
        return Stream.of(
                Arguments.of(new UserRequestDTO(null, "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // null
                Arguments.of(new UserRequestDTO("", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // пустая строка
                Arguments.of(new UserRequestDTO("Jo", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // менее 3 символов
                Arguments.of(new UserRequestDTO("J".repeat(51), "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // больше 50 символов
                Arguments.of(new UserRequestDTO("Username", null, "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // null
                Arguments.of(new UserRequestDTO("Username", "", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // пустая строка
                Arguments.of(new UserRequestDTO("Username", "J", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // менее 2 символов
                Arguments.of(new UserRequestDTO("Username", "J".repeat(51), "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // больше 50 символов
                Arguments.of(new UserRequestDTO("Username", "John", null, "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // null
                Arguments.of(new UserRequestDTO("Username", "John", "", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // пустая строка
                Arguments.of(new UserRequestDTO("Username", "John", "D", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // менее 2 символов
                Arguments.of(new UserRequestDTO("Username", "John", "D".repeat(51), "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // больше 50 символов
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", null, "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // null
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // пустая строка
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "invalid-email", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")), // невалидный email
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", null, "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "short1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "long".repeat(6) + "1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "PASSWORD1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", null, LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", null, "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.now().plusDays(1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), null)),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "1234567890")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+7123456789")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+712345678901")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+7abcdefghij"))
        );
    }
}
