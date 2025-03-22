package ru.effectmobile.task_management_system.service.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.effectmobile.task_management_system.config.crypto.CipherService;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.model.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.util.DefaultInputs.EMAIL_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ENCRYPTED_EMAIL_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ENCRYPTED_PHONE_NUMBER_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ENCRYPTED_USERNAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.PHONE_NUMBER_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.USERNAME_EXAMPLE;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private CipherService cipherService;

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void userToResponseDTO_ShouldDecryptFieldsCorrectly() {
        User user = User.builder()
                .username(ENCRYPTED_USERNAME_EXAMPLE)
                .email(ENCRYPTED_EMAIL_EXAMPLE)
                .phoneNumber(ENCRYPTED_PHONE_NUMBER_EXAMPLE)
                .build();

        when(cipherService.decrypt(ENCRYPTED_USERNAME_EXAMPLE)).thenReturn(USERNAME_EXAMPLE);
        when(cipherService.decrypt(ENCRYPTED_EMAIL_EXAMPLE)).thenReturn(EMAIL_EXAMPLE);
        when(cipherService.decrypt(ENCRYPTED_PHONE_NUMBER_EXAMPLE)).thenReturn(PHONE_NUMBER_EXAMPLE);

        UserResponseDTO dto = mapper.userToResponseDTO(user, cipherService);

        assertEquals(USERNAME_EXAMPLE, dto.username());
        assertEquals(EMAIL_EXAMPLE, dto.email());
        assertEquals(PHONE_NUMBER_EXAMPLE, dto.phoneNumber());

        verify(cipherService).decrypt(user.getUsername());
        verify(cipherService).decrypt(user.getEmail());
        verify(cipherService).decrypt(user.getPhoneNumber());
    }
}
