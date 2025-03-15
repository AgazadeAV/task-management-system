package ru.effectmobile.task_management_system.config.swagger.schemas.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import static ru.effectmobile.task_management_system.util.DefaultInputs.*;

@Getter
public class CommentResponseDTOSchema {

    @Schema(example = COMMENT_ID_EXAMPLE_JSON, description = "Unique identifier of the comment.")
    private String id;

    @Schema(example = TASK_ID_EXAMPLE_JSON, description = "ID of the task to which the comment belongs.")
    private String taskId;

    @Schema(example = TASK_TITLE_EXAMPLE, description = "Title of the task associated with the comment.")
    private String taskTitle;

    @Schema(example = USER_ID_EXAMPLE_JSON, description = "ID of the user who created the comment.")
    private String authorId;

    @Schema(example = FIRST_NAME_EXAMPLE + " " + LAST_NAME_EXAMPLE, description = "Full name of the comment author.")
    private String authorFullName;

    @Schema(example = COMMENT_TEXT_EXAMPLE, description = "Content of the comment.")
    private String text;

    @Schema(example = DATE_EXAMPLE, description = "Timestamp when the comment was created.")
    private String createdAt;

    @Schema(example = DATE_EXAMPLE, description = "Timestamp when the comment was last updated.")
    private String updatedAt;
}
