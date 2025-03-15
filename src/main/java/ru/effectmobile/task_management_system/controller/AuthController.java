package ru.effectmobile.task_management_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectmobile.task_management_system.dto.requests.LoginRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.AuthResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.service.facade.UserFacade;
import ru.effectmobile.task_management_system.config.swagger.specs.AuthApiSpec;

@RestController
@RequestMapping("${api.base.url}" + AuthController.AUTH_API_URI)
@RequiredArgsConstructor
public class AuthController implements AuthApiSpec {

    public static final String AUTH_API_URI = "/auth";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";

    private final UserFacade userFacade;

    @PostMapping(LOGIN)
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = userFacade.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(REGISTER)
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO response = userFacade.register(request);
        return ResponseEntity.ok(response);
    }
}
