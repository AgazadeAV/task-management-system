package ru.effectmobile.task_management_system.service.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.effectmobile.task_management_system.dto.requests.LoginRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.AuthResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;

import java.util.UUID;

public interface UserFacade {

    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    UserResponseDTO getUserById(UUID id);

    UserResponseDTO createUser(UserRequestDTO userDTO);

    AuthResponseDTO login(LoginRequestDTO loginDTO);

    UserResponseDTO register(UserRequestDTO request);
}
