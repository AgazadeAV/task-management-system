package ru.effectmobile.task_management_system.service.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAuthorUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createComment;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTask;

class CommentMapperTest {

    private final CommentMapper mapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void commentToResponseDTO_ShouldMapCorrectly() {
        Task task = createTask(TaskStatus.COMPLETED, TaskPriority.LOW);
        User author = createAuthorUser(Role.ROLE_ADMIN);
        Comment comment = createComment(task, author);

        CommentResponseDTO dto = mapper.commentToResponseDTO(comment);

        assertEquals(comment.getId(), dto.id());
        assertEquals(task.getId(), dto.taskId());
        assertEquals(task.getTitle(), dto.taskTitle());
        assertEquals(author.getId(), dto.authorId());
        assertEquals(comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName(), dto.authorFullName());
        assertEquals(comment.getText(), dto.text());
        assertEquals(comment.getMetaData().getCreatedAt(), dto.createdAt());
        assertEquals(comment.getMetaData().getUpdatedAt(), dto.updatedAt());
    }
}
