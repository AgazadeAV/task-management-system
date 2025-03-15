package ru.effectmobile.task_management_system.config.swagger.schemas.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import static ru.effectmobile.task_management_system.util.DefaultInputs.ASSIGNEE_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.AUTHOR_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.DATE_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.FIRST_NAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.LAST_NAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_DESCRIPTION_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_ID_EXAMPLE_JSON;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_PRIORITY_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_STATUS_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_TITLE_EXAMPLE;

@Getter
public class TaskResponseDTOSchema {

    @Schema(example = TASK_ID_EXAMPLE_JSON, description = "Unique identifier of the task.")
    private String id;

    @Schema(example = TASK_TITLE_EXAMPLE, description = "Title of the task.")
    private String title;

    @Schema(example = TASK_DESCRIPTION_EXAMPLE, description = "Detailed description of the task.")
    private String description;

    @Schema(example = TASK_STATUS_EXAMPLE, description = "Current status of the task.")
    private String status;

    @Schema(example = TASK_PRIORITY_EXAMPLE, description = "Priority level of the task.")
    private String priority;

    @Schema(example = AUTHOR_ID_EXAMPLE_JSON, description = "ID of the task author.")
    private String authorId;

    @Schema(example = FIRST_NAME_EXAMPLE + " " + LAST_NAME_EXAMPLE, description = "Full name of the task author.")
    private String authorFullName;

    @Schema(example = ASSIGNEE_ID_EXAMPLE_JSON, description = "ID of the task assignee.")
    private String assigneeId;

    @Schema(example = FIRST_NAME_EXAMPLE + " " + LAST_NAME_EXAMPLE, description = "Full name of the task assignee.")
    private String assigneeFullName;

    @Schema(example = DATE_EXAMPLE, description = "Timestamp when the task was created.")
    private String createdAt;

    @Schema(example = DATE_EXAMPLE, description = "Timestamp when the task was last updated.")
    private String updatedAt;
}
