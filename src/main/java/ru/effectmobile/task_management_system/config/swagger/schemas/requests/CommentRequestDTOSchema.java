package ru.effectmobile.task_management_system.config.swagger.schemas.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.COMMENT_TEXT_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_ID_EXAMPLE_JSON;

@Getter
@Builder
public final class CommentRequestDTOSchema {

    @Schema(example = TASK_ID_EXAMPLE_JSON, description = "Task ID to which the comment belongs.")
    private final String taskId;

    @Schema(example = COMMENT_TEXT_EXAMPLE, description = "Text of the comment.")
    private final String text;
}
