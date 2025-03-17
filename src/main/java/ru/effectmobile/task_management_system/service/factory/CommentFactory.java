package ru.effectmobile.task_management_system.service.factory;

import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.metadata.MetaData;

@Component
public class CommentFactory {

    public Comment createComment(CommentRequestDTO dto, Task task, User author, MetaData metaData) {
        return Comment.builder()
                .task(task)
                .author(author)
                .text(dto.text())
                .metaData(metaData)
                .build();
    }
}
