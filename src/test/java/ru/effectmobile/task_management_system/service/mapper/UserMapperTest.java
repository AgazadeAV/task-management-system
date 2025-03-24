package ru.effectmobile.task_management_system.service.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.effectmobile.task_management_system.config.crypto.CipherService;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.util.DefaultInputs.EMAIL_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ENCRYPTED_EMAIL_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ENCRYPTED_PHONE_NUMBER_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ENCRYPTED_USERNAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.PHONE_NUMBER_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.USERNAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUser;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private CipherService cipherService;

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void userToResponseDTO_ShouldDecryptFieldsCorrectly() {
        User user = createUser(Role.ROLE_ADMIN);
        user.setUsername(ENCRYPTED_USERNAME_EXAMPLE);
        user.setEmail(ENCRYPTED_EMAIL_EXAMPLE);
        user.setPhoneNumber(ENCRYPTED_PHONE_NUMBER_EXAMPLE);

        when(cipherService.decrypt(user.getUsername())).thenReturn(USERNAME_EXAMPLE);
        when(cipherService.decrypt(user.getEmail())).thenReturn(EMAIL_EXAMPLE);
        when(cipherService.decrypt(user.getPhoneNumber())).thenReturn(PHONE_NUMBER_EXAMPLE);

        UserResponseDTO dto = mapper.userToResponseDTO(user, cipherService);

        assertEquals(user.getId(), dto.id());
        assertEquals(USERNAME_EXAMPLE, dto.username());
        assertEquals(user.getFirstName(), dto.firstName());
        assertEquals(user.getLastName(), dto.lastName());
        assertEquals(EMAIL_EXAMPLE, dto.email());
        assertEquals(PHONE_NUMBER_EXAMPLE, dto.phoneNumber());
        assertEquals(user.getRole(), dto.role());

        verify(cipherService).decrypt(user.getUsername());
        verify(cipherService).decrypt(user.getEmail());
        verify(cipherService).decrypt(user.getPhoneNumber());
    }
}
