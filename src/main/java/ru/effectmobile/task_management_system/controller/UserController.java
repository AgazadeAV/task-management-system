package ru.effectmobile.task_management_system.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.effectmobile.task_management_system.config.swagger.specs.UserApiSpec;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.service.facade.UserFacade;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${api.base.url}" + UserController.USER_API_URL)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController implements UserApiSpec {

    public static final String USER_API_URL = "/users";
    public static final String GET_ALL_USERS = "/users-list";
    public static final String GET_USER_BY_ID = "/user/{id}";
    public static final String CREATE_USER = "/create-user";

    private final UserFacade userFacade;

    @GetMapping(GET_ALL_USERS)
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(@ParameterObject Pageable pageable) {
        log.info("Fetching all users with pagination: {}", pageable);
        Page<UserResponseDTO> response = userFacade.getAllUsers(pageable);
        log.info("Retrieved {} users", response.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping(GET_USER_BY_ID)
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") UUID id) {
        log.info("Fetching user by ID '{}'", id);
        UserResponseDTO response = userFacade.getUserById(id);
        log.info("User retrieved: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping(CREATE_USER)
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Creating a new user: {}", userRequestDTO);
        UserResponseDTO response = userFacade.createUser(userRequestDTO);
        log.info("User created successfully with ID '{}'", response.id());
        return ResponseEntity.ok(response);
    }
}
