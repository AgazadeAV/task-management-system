package ru.effectmobile.task_management_system.service.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.model.metadata.MetaData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAsigneeUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAuthorUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createMetaData;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTask;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTaskRequestDTO;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {

    private final TaskMapper mapper = Mappers.getMapper(TaskMapper.class);

    @Test
    void taskToResponseDTO_ShouldMapCorrectly() {
        User author = createAuthorUser(Role.ROLE_ADMIN);
        User assignee = createAsigneeUser(Role.ROLE_USER);
        MetaData metaData = createMetaData();
        Task task = createTask(TaskStatus.COMPLETED, TaskPriority.LOW);
        task.setAssignee(assignee);
        task.setAuthor(author);

        TaskResponseDTO dto = mapper.taskToResponseDTO(task);

        assertEquals(author.getId(), dto.authorId());
        assertEquals(author.getFirstName() + " " + author.getLastName(), dto.authorFullName());
        assertEquals(assignee.getId(), dto.assigneeId());
        assertEquals(assignee.getFirstName() + " " + assignee.getLastName(), dto.assigneeFullName());
        assertEquals(metaData.getCreatedAt(), dto.createdAt());
    }

    @Test
    void updateTaskFromRequestDTO_ShouldUpdateFields() {
        Task task = createTask(TaskStatus.COMPLETED, TaskPriority.LOW);
        TaskRequestDTO dto = createTaskRequestDTO();

        mapper.updateTaskFromRequestDTO(dto, task);

        assertEquals(dto.title(), task.getTitle());
        assertEquals(dto.description(), task.getDescription());
    }
}
