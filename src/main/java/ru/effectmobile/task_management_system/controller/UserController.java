package ru.effectmobile.task_management_system.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.service.facade.UserFacade;
import ru.effectmobile.task_management_system.config.swagger.specs.UserApiSpec;

import java.util.UUID;

@RestController
@RequestMapping("${api.base.url}" + UserController.USER_API_URI)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController implements UserApiSpec {

    public static final String USER_API_URI = "/users";
    public static final String GET_ALL_USERS = "/users-list";
    public static final String GET_USER_BY_ID = "/user/{id}";
    public static final String CREATE_USER = "/create-user";

    private final UserFacade userFacade;

    @GetMapping(GET_ALL_USERS)
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userFacade.getAllUsers(pageable));
    }

    @GetMapping(GET_USER_BY_ID)
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(userFacade.getUserById(id));
    }

    @PostMapping(CREATE_USER)
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userFacade.createUser(userRequestDTO));
    }
}
