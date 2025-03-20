package ru.effectmobile.task_management_system.util;

import ru.effectmobile.task_management_system.dto.filters.TaskFilterDTO;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.LoginRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;
import ru.effectmobile.task_management_system.dto.responses.AuthResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.CommentResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.TaskResponseDTO;
import ru.effectmobile.task_management_system.dto.responses.UserResponseDTO;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.model.metadata.MetaData;

import java.util.ArrayList;
import java.util.UUID;

import static ru.effectmobile.task_management_system.util.DefaultInputs.ASSIGNEE_ID_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.AUTHOR_ID_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.BIRTH_DATE_EXAMPLE_LOCALDATE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.COMMENT_ID_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.COMMENT_TEXT_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.CREATION_DATE_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.EMAIL_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.FIRST_NAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.LAST_NAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.PASSWORD_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.PHONE_NUMBER_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ROLE_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.ROLE_EXAMPLE_ENUM;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_DESCRIPTION_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_ID_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_PRIORITY_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_PRIORITY_EXAMPLE_ENUM;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_STATUS_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_STATUS_EXAMPLE_ENUM;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TASK_TITLE_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.TOKEN_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.UPDATE_DATE_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.USERNAME_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.USER_ID_EXAMPLE;
import static ru.effectmobile.task_management_system.util.DefaultInputs.VERSION_EXAMPLE;

public class ModelCreator {

    public static User createUser(Role role) {
        return User.builder()
                .id(USER_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .metaData(createMetaData())
                .username(USERNAME_EXAMPLE)
                .firstName(FIRST_NAME_EXAMPLE)
                .lastName(LAST_NAME_EXAMPLE)
                .email(EMAIL_EXAMPLE)
                .password(PASSWORD_EXAMPLE)
                .role(role)
                .birthDate(BIRTH_DATE_EXAMPLE_LOCALDATE)
                .phoneNumber(PHONE_NUMBER_EXAMPLE)
                .tasks(new ArrayList<>())
                .createdTasks(new ArrayList<>())
                .build();
    }

    public static User createAuthorUser(Role role) {
        return User.builder()
                .id(AUTHOR_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .metaData(createMetaData())
                .username(USERNAME_EXAMPLE)
                .firstName(FIRST_NAME_EXAMPLE)
                .lastName(LAST_NAME_EXAMPLE)
                .email(EMAIL_EXAMPLE)
                .password(PASSWORD_EXAMPLE)
                .role(role)
                .birthDate(BIRTH_DATE_EXAMPLE_LOCALDATE)
                .phoneNumber(PHONE_NUMBER_EXAMPLE)
                .tasks(new ArrayList<>())
                .createdTasks(new ArrayList<>())
                .build();
    }

    public static User createAsigneeUser(Role role) {
        return User.builder()
                .id(ASSIGNEE_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .metaData(createMetaData())
                .username(USERNAME_EXAMPLE)
                .firstName(FIRST_NAME_EXAMPLE)
                .lastName(LAST_NAME_EXAMPLE)
                .email(EMAIL_EXAMPLE)
                .password(PASSWORD_EXAMPLE)
                .role(role)
                .birthDate(BIRTH_DATE_EXAMPLE_LOCALDATE)
                .phoneNumber(PHONE_NUMBER_EXAMPLE)
                .tasks(new ArrayList<>())
                .createdTasks(new ArrayList<>())
                .build();
    }

    public static Task createTask(TaskStatus status, TaskPriority priority) {
        return Task.builder()
                .id(TASK_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .metaData(createMetaData())
                .title(TASK_TITLE_EXAMPLE)
                .description(TASK_DESCRIPTION_EXAMPLE)
                .status(status)
                .priority(priority)
                .comments(new ArrayList<>())
                .build();
    }

    public static Task createTask(TaskStatus status, TaskPriority priority, User author) {
        return Task.builder()
                .id(TASK_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .metaData(createMetaData())
                .title(TASK_TITLE_EXAMPLE)
                .description(TASK_DESCRIPTION_EXAMPLE)
                .status(status)
                .priority(priority)
                .author(author)
                .comments(new ArrayList<>())
                .build();
    }

    public static Task createTask(TaskStatus status, TaskPriority priority, User author, User assignee) {
        return Task.builder()
                .id(TASK_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .metaData(createMetaData())
                .title(TASK_TITLE_EXAMPLE)
                .description(TASK_DESCRIPTION_EXAMPLE)
                .status(status)
                .priority(priority)
                .author(author)
                .assignee(assignee)
                .comments(new ArrayList<>())
                .build();
    }

    public static Comment createComment() {
        return Comment.builder()
                .id(COMMENT_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .text(COMMENT_TEXT_EXAMPLE)
                .build();
    }

    public static Comment createComment(Task task) {
        return Comment.builder()
                .id(COMMENT_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .text(COMMENT_TEXT_EXAMPLE)
                .task(task)
                .build();
    }

    public static Comment createComment(User author) {
        return Comment.builder()
                .id(COMMENT_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .text(COMMENT_TEXT_EXAMPLE)
                .author(author)
                .build();
    }

    public static Comment createComment(Task task, User author) {
        return Comment.builder()
                .id(COMMENT_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .text(COMMENT_TEXT_EXAMPLE)
                .task(task)
                .author(author)
                .build();
    }

    public static MetaData createMetaData() {
        return MetaData.builder()
                .createdAt(CREATION_DATE_EXAMPLE)
                .updatedAt(UPDATE_DATE_EXAMPLE)
                .build();
    }

    public static TaskFilterDTO createTaskFilterDTO() {
        return new TaskFilterDTO(
                AUTHOR_ID_EXAMPLE,
                ASSIGNEE_ID_EXAMPLE,
                TASK_STATUS_EXAMPLE_ENUM,
                TASK_PRIORITY_EXAMPLE_ENUM
        );
    }

    public static TaskFilterDTO createTaskFilterDTO(UUID authorId, UUID assigneeId, TaskStatus status, TaskPriority priority) {
        return new TaskFilterDTO(
                authorId,
                assigneeId,
                status,
                priority
        );
    }

    public static CommentRequestDTO createCommentRequestDTO() {
        return new CommentRequestDTO(
                TASK_ID_EXAMPLE,
                COMMENT_TEXT_EXAMPLE
        );
    }

    public static LoginRequestDTO createLoginRequestDTO() {
        return new LoginRequestDTO(
                EMAIL_EXAMPLE,
                PASSWORD_EXAMPLE
        );
    }

    public static TaskRequestDTO createTaskRequestDTO() {
        return new TaskRequestDTO(
                TASK_TITLE_EXAMPLE,
                TASK_DESCRIPTION_EXAMPLE,
                TASK_STATUS_EXAMPLE,
                TASK_PRIORITY_EXAMPLE,
                ASSIGNEE_ID_EXAMPLE
        );
    }

    public static UserRequestDTO createUserRequestDTO() {
        return new UserRequestDTO(
                USERNAME_EXAMPLE,
                FIRST_NAME_EXAMPLE,
                LAST_NAME_EXAMPLE,
                EMAIL_EXAMPLE,
                PASSWORD_EXAMPLE,
                ROLE_EXAMPLE,
                BIRTH_DATE_EXAMPLE_LOCALDATE,
                PHONE_NUMBER_EXAMPLE
        );
    }

    public static AuthResponseDTO createAuthResponseDTO() {
        return new AuthResponseDTO(
                TOKEN_EXAMPLE
        );
    }

    public static CommentResponseDTO createCommentResponseDTO() {
        return new CommentResponseDTO(
                COMMENT_ID_EXAMPLE,
                TASK_ID_EXAMPLE,
                TASK_TITLE_EXAMPLE,
                AUTHOR_ID_EXAMPLE,
                FIRST_NAME_EXAMPLE + " " + LAST_NAME_EXAMPLE,
                COMMENT_TEXT_EXAMPLE,
                CREATION_DATE_EXAMPLE,
                UPDATE_DATE_EXAMPLE
        );
    }

    public static TaskResponseDTO createTaskResponseDTO() {
        return new TaskResponseDTO(
                TASK_ID_EXAMPLE,
                TASK_TITLE_EXAMPLE,
                TASK_DESCRIPTION_EXAMPLE,
                TASK_STATUS_EXAMPLE_ENUM,
                TASK_PRIORITY_EXAMPLE_ENUM,
                AUTHOR_ID_EXAMPLE,
                FIRST_NAME_EXAMPLE + " " + LAST_NAME_EXAMPLE,
                ASSIGNEE_ID_EXAMPLE,
                FIRST_NAME_EXAMPLE + " " + LAST_NAME_EXAMPLE,
                CREATION_DATE_EXAMPLE,
                UPDATE_DATE_EXAMPLE
        );
    }

    public static UserResponseDTO createUserResponseDTO() {
        return new UserResponseDTO(
                USER_ID_EXAMPLE,
                USERNAME_EXAMPLE,
                FIRST_NAME_EXAMPLE,
                LAST_NAME_EXAMPLE,
                EMAIL_EXAMPLE,
                PHONE_NUMBER_EXAMPLE,
                ROLE_EXAMPLE_ENUM
        );
    }
}
