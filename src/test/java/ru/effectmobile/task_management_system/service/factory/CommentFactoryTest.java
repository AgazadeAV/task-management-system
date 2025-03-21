package ru.effectmobile.task_management_system.service.factory;

import org.junit.jupiter.api.Test;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.model.metadata.MetaData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAuthorUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createCommentRequestDTO;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTask;

public class CommentFactoryTest {

    private final CommentFactory commentFactory = new CommentFactory();
    private final MetaDataFactory metaDataFactory = new MetaDataFactory();

    @Test
    void createComment_ShouldReturnComment() {
        CommentRequestDTO dto = createCommentRequestDTO();
        Task task = createTask(TaskStatus.COMPLETED, TaskPriority.MEDIUM);
        User user = createAuthorUser(Role.ROLE_USER);
        MetaData metaData = metaDataFactory.createMetaData();

        Comment comment = commentFactory.createComment(dto, task, user, metaData);

        assertNotNull(comment);
        assertEquals(dto.text(), comment.getText());
        assertEquals(task, comment.getTask());
        assertEquals(user, comment.getAuthor());
        assertEquals(metaData, comment.getMetaData());
    }
}
