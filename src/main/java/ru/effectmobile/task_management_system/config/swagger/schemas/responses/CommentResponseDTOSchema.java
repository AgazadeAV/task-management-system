package ru.effectmobile.task_management_system.config.swagger.schemas.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.COMMENT_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.COMMENT_TEXT_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.DATE_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.FIRST_NAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.LAST_NAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_TITLE_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.USER_ID_EXAMPLE_JSON;

@Getter
@Builder
public final class CommentResponseDTOSchema {

    @Schema(example = COMMENT_ID_EXAMPLE_JSON, description = "Unique identifier of the comment.")
    private final String id;

    @Schema(example = TASK_ID_EXAMPLE_JSON, description = "ID of the task to which the comment belongs.")
    private final String taskId;

    @Schema(example = TASK_TITLE_EXAMPLE, description = "Title of the task associated with the comment.")
    private final String taskTitle;

    @Schema(example = USER_ID_EXAMPLE_JSON, description = "ID of the user who created the comment.")
    private final String authorId;

    @Schema(example = FIRST_NAME_EXAMPLE + " " + LAST_NAME_EXAMPLE, description = "Full name of the comment author.")
    private final String authorFullName;

    @Schema(example = COMMENT_TEXT_EXAMPLE, description = "Content of the comment.")
    private final String text;

    @Schema(example = DATE_EXAMPLE, description = "Timestamp when the comment was created.")
    private final String createdAt;

    @Schema(example = DATE_EXAMPLE, description = "Timestamp when the comment was last updated.")
    private final String updatedAt;
}
