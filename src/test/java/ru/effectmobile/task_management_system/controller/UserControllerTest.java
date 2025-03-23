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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.exception.custom.notfound.UserNotFoundException;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.service.facade.UserFacade;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.effectmobile.task_management_system.controller.UserController.CREATE_USER;
import static ru.effectmobile.task_management_system.controller.UserController.GET_ALL_USERS;
import static ru.effectmobile.task_management_system.controller.UserController.GET_USER_BY_ID;
import static ru.effectmobile.task_management_system.controller.UserController.USER_API_URL;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.USER_NOT_FOUND_BY_ID_MESSAGE;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUserRequestDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUserResponseDTO;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserFacade userFacade;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    private String apiPathPrefix;

    private static final UserResponseDTO USER_RESPONSE_DTO = createUserResponseDTO();
    private static final Page<UserResponseDTO> USERS = new PageImpl<>(List.of(USER_RESPONSE_DTO, USER_RESPONSE_DTO));
    private static final UserRequestDTO USER_REQUEST_DTO = createUserRequestDTO();
    private static final User USER = createUser(Role.ROLE_ADMIN);

    @BeforeEach
    void setUp() {
        this.apiPathPrefix = apiBaseUrl + USER_API_URL;
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getAllUsers_Success() throws Exception {
        when(userFacade.getAllUsers(any(Pageable.class))).thenReturn(USERS);

        mockMvc.perform(get(apiPathPrefix + GET_ALL_USERS))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers_Unauthorized() throws Exception {
        mockMvc.perform(get(apiPathPrefix + GET_ALL_USERS))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getAllUsers_Forbidden() throws Exception {
        when(userFacade.getAllUsers(any(Pageable.class))).thenReturn(USERS);

        mockMvc.perform(get(apiPathPrefix + GET_ALL_USERS))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getUserById_Success() throws Exception {
        when(userFacade.getUserById(any(UUID.class))).thenReturn(USER_RESPONSE_DTO);

        mockMvc.perform(get(apiPathPrefix + GET_USER_BY_ID, USER.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_Unauthorized() throws Exception {
        mockMvc.perform(get(apiPathPrefix + GET_USER_BY_ID, USER.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getUserById_Forbidden() throws Exception {
        when(userFacade.getUserById(any(UUID.class))).thenReturn(USER_RESPONSE_DTO);

        mockMvc.perform(get(apiPathPrefix + GET_USER_BY_ID, USER.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getUserById_NotFound() throws Exception {
        when(userFacade.getUserById(any(UUID.class)))
                .thenThrow(new UserNotFoundException(String.format(USER_NOT_FOUND_BY_ID_MESSAGE, USER.getId())));

        mockMvc.perform(get(apiPathPrefix + GET_USER_BY_ID, USER.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void createUser_Success() throws Exception {
        when(userFacade.createUser(any(UserRequestDTO.class))).thenReturn(USER_RESPONSE_DTO);

        mockMvc.perform(post(apiPathPrefix + CREATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(USER_REQUEST_DTO)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserRequests")
    @WithMockUser(authorities = "ROLE_ADMIN")
    void createUser_BadRequest(UserRequestDTO invalidRequest) throws Exception {
        mockMvc.perform(post(apiPathPrefix + CREATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_Unauthorized() throws Exception {
        mockMvc.perform(post(apiPathPrefix + CREATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(USER_REQUEST_DTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void createUser_Forbidden() throws Exception {
        when(userFacade.createUser(any(UserRequestDTO.class))).thenReturn(USER_RESPONSE_DTO);

        mockMvc.perform(post(apiPathPrefix + CREATE_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(USER_REQUEST_DTO)))
                .andExpect(status().isForbidden());
    }

    private static Stream<Arguments> provideInvalidUserRequests() {
        return Stream.of(
                Arguments.of(new UserRequestDTO(null, "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Jo", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("J".repeat(51), "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", null, "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "J", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "J".repeat(51), "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", null, "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "D", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "D".repeat(51), "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", null, "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "invalid-email", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
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
