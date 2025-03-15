package ru.effectmobile.task_management_system.service.factory;

import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.metadata.MetaData;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;

@Component
public class CommentFactory {

    public Comment createComment(CommentRequestDTO dto, Task task, User author) {
        return Comment.builder()
                .task(task)
                .author(author)
                .text(dto.text())
                .metaData(new MetaData())
                .build();
    }
}
