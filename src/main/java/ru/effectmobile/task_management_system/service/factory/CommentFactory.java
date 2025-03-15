package ru.effectmobile.task_management_system.service.factory;

import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.model.Comment;
import ru.effectmobile.task_management_system.model.MetaData;
import ru.effectmobile.task_management_system.model.Task;
import ru.effectmobile.task_management_system.model.User;

@Component
public class CommentFactory {

    public Comment createComment(CommentRequestDTO dto, Task task, User author) {
        return Comment.builder()
                .task(task)
                .author(author)
                .text(dto.getText())
                .metaData(new MetaData())
                .build();
    }
}
