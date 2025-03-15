package ru.effectmobile.task_management_system.swagger.schemas.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.ASSIGNEE_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.AUTHOR_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_DESCRIPTION_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_PRIORITY_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_STATUS_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_TITLE_EXAMPLE;

@Getter
public class TaskRequestDTOSchema {

    @Schema(example = TASK_TITLE_EXAMPLE, description = "Title of the task.")
    private String title;

    @Schema(example = TASK_DESCRIPTION_EXAMPLE, description = "Description of the task.")
    private String description;

    @Schema(example = TASK_STATUS_EXAMPLE, description = "Current status of the task.")
    private String status;

    @Schema(example = TASK_PRIORITY_EXAMPLE, description = "Priority level of the task.")
    private String priority;

    @Schema(example = AUTHOR_ID_EXAMPLE_JSON, description = "Author ID of the task.")
    private String authorId;

    @Schema(example = ASSIGNEE_ID_EXAMPLE_JSON, description = "Assignee ID of the task.")
    private String assigneeId;
}
