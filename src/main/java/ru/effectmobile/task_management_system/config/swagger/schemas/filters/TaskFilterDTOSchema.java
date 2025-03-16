package ru.effectmobile.task_management_system.config.swagger.schemas.filters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.ASSIGNEE_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.AUTHOR_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_PRIORITY_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_STATUS_EXAMPLE;

@Getter
@Builder
public final class TaskFilterDTOSchema {

    @Schema(example = AUTHOR_ID_EXAMPLE_JSON, description = "Author ID of the task.")
    private final String authorId;

    @Schema(example = ASSIGNEE_ID_EXAMPLE_JSON, description = "Assignee ID of the task.")
    private final String assigneeId;

    @Schema(example = TASK_STATUS_EXAMPLE, description = "Current status of the task.")
    private final String status;

    @Schema(example = TASK_PRIORITY_EXAMPLE, description = "Priority level of the task.")
    private final String priority;
}
