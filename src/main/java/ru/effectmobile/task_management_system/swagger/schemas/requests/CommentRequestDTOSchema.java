package ru.effectmobile.task_management_system.swagger.schemas.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.AUTHOR_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.COMMENT_TEXT_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_ID_EXAMPLE_JSON;

@Getter
public class CommentRequestDTOSchema {

    @Schema(example = TASK_ID_EXAMPLE_JSON, description = "Task ID to which the comment belongs.")
    private String taskId;

    @Schema(example = AUTHOR_ID_EXAMPLE_JSON, description = "Author ID who created the comment.")
    private String authorId;

    @Schema(example = COMMENT_TEXT_EXAMPLE, description = "Text of the comment.")
    private String text;
}
