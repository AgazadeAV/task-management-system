package ru.effectmobile.task_management_system.config.swagger.schemas.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.ASSIGNEE_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_DESCRIPTION_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_PRIORITY_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_STATUS_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_TITLE_EXAMPLE;

@Getter
@Builder
public final class TaskRequestDTOSchema {

    @Schema(example = TASK_TITLE_EXAMPLE, description = "Title of the task.")
    private final String title;

    @Schema(example = TASK_DESCRIPTION_EXAMPLE, description = "Description of the task.")
    private final String description;

    @Schema(example = TASK_STATUS_EXAMPLE, description = "Current status of the task.")
    private final String status;

    @Schema(example = TASK_PRIORITY_EXAMPLE, description = "Priority level of the task.")
    private final String priority;

    @Schema(example = ASSIGNEE_ID_EXAMPLE_JSON, description = "Assignee ID of the task.")
    private final String assigneeId;
}
