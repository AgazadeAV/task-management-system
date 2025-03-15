package ru.effectmobile.task_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.service.TaskManagementFacade;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.base.url}" + UserController.USER_API_URI)
@RequiredArgsConstructor
public class UserController {

    public static final String USER_API_URI = "/users";
    public static final String GET_ALL_USERS = "/users-list";
    public static final String GET_USER_BY_ID = "/user/{id}";
    public static final String CREATE_USER = "/create-user";

    private final TaskManagementFacade taskManagementFacade;

    @GetMapping(GET_ALL_USERS)
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(taskManagementFacade.getAllUsers());
    }

    @GetMapping(GET_USER_BY_ID)
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(taskManagementFacade.getUserById(id));
    }

    @PostMapping(CREATE_USER)
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(taskManagementFacade.createUser(userRequestDTO));
    }
}
