package ru.effectmobile.task_management_system.service.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.metadata.MetaData;

@Slf4j
@Component
public class CommentFactory {

    public Comment createComment(CommentRequestDTO dto, Task task, User author, MetaData metaData) {
        log.debug("Creating comment for task ID: {} by author ID: {}", task.getId(), author.getId());

        Comment comment = Comment.builder()
                .task(task)
                .author(author)
                .text(dto.text())
                .metaData(metaData)
                .build();

        log.debug("Comment created successfully for task ID: {}", task.getId());
        return comment;
    }
}
