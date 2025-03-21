package ru.effectmobile.task_management_system.service.factory;

import org.junit.jupiter.api.Test;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.model.metadata.MetaData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAuthorUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTaskRequestDTO;

class TaskFactoryTest {

    private final TaskFactory taskFactory = new TaskFactory();
    private final MetaDataFactory metaDataFactory = new MetaDataFactory();

    @Test
    void createTask_ShouldReturnTask() {
        TaskRequestDTO dto = createTaskRequestDTO();
        User author = createAuthorUser(Role.ROLE_ADMIN);
        MetaData metaData = metaDataFactory.createMetaData();

        Task task = taskFactory.createTask(dto, author, metaData);

        assertNotNull(task);
        assertEquals(dto.title(), task.getTitle());
        assertEquals(dto.description(), task.getDescription());
        assertEquals(author, task.getAuthor());
        assertEquals(metaData, task.getMetaData());
        assertEquals(TaskPriority.valueOf(dto.priority()), task.getPriority());
        assertEquals(TaskStatus.valueOf(dto.status()), task.getStatus());
    }
}
