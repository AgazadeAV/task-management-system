package ru.effectmobile.task_management_system.service.factory;

import org.junit.jupiter.api.Test;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.metadata.MetaData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUserRequestDTO;

public class UserFactoryTest {

    private final UserFactory userFactory = new UserFactory();
    private final MetaDataFactory metaDataFactory = new MetaDataFactory();

    @Test
    void createUser_ShouldReturnUser() {
        UserRequestDTO dto = createUserRequestDTO();
        MetaData metaData = metaDataFactory.createMetaData();

        User user = userFactory.createUser(dto, metaData);

        assertNotNull(user);
        assertEquals(dto.username(), user.getUsername());
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.firstName(), user.getFirstName());
        assertEquals(dto.lastName(), user.getLastName());
        assertEquals(dto.password(), user.getPassword());
        assertEquals(dto.phoneNumber(), user.getPhoneNumber());
        assertEquals(dto.birthDate(), user.getBirthDate());
        assertEquals(Role.valueOf(dto.role()), user.getRole());
        assertEquals(metaData, user.getMetaData());
    }
}
