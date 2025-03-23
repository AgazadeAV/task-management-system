package ru.effectmobile.task_management_system.util;

import org.junit.jupiter.params.provider.Arguments;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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
                .metaData(createMetaData())
                .text(COMMENT_TEXT_EXAMPLE)
                .build();
    }

    public static Comment createComment(Task task) {
        return Comment.builder()
                .id(COMMENT_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .metaData(createMetaData())
                .text(COMMENT_TEXT_EXAMPLE)
                .task(task)
                .build();
    }

    public static Comment createComment(User author) {
        return Comment.builder()
                .id(COMMENT_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .metaData(createMetaData())
                .text(COMMENT_TEXT_EXAMPLE)
                .author(author)
                .build();
    }

    public static Comment createComment(Task task, User author) {
        return Comment.builder()
                .id(COMMENT_ID_EXAMPLE)
                .version(VERSION_EXAMPLE)
                .metaData(createMetaData())
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

    public static Stream<Arguments> provideInvalidCommentRequests() {
        return Stream.of(
                Arguments.of(new CommentRequestDTO(null, "Valid comment")),
                Arguments.of(new CommentRequestDTO(UUID.randomUUID(), "")),
                Arguments.of(new CommentRequestDTO(UUID.randomUUID(), "  ")),
                Arguments.of(new CommentRequestDTO(UUID.randomUUID(), "A".repeat(2001)))
        );
    }

    public static Stream<Arguments> provideInvalidTaskRequests() {
        return Stream.of(
                Arguments.of(new TaskRequestDTO(null, "Valid description", "TODO", "HIGH", UUID.randomUUID())),
                Arguments.of(new TaskRequestDTO("", "Valid description", "TODO", "HIGH", UUID.randomUUID())),
                Arguments.of(new TaskRequestDTO("   ", "Valid description", "TODO", "HIGH", UUID.randomUUID())),
                Arguments.of(new TaskRequestDTO("A".repeat(256), "Valid description", "TODO", "HIGH", UUID.randomUUID())),
                Arguments.of(new TaskRequestDTO("Valid title", null, "TODO", "HIGH", UUID.randomUUID())),
                Arguments.of(new TaskRequestDTO("Valid title", "", "TODO", "HIGH", UUID.randomUUID())),
                Arguments.of(new TaskRequestDTO("Valid title", "   ", "TODO", "HIGH", UUID.randomUUID())),
                Arguments.of(new TaskRequestDTO("Valid title", "A".repeat(1001), "TODO", "HIGH", UUID.randomUUID())),
                Arguments.of(new TaskRequestDTO("Valid title", "Valid description", null, "HIGH", UUID.randomUUID())),
                Arguments.of(new TaskRequestDTO("Valid title", "Valid description", "TODO", null, UUID.randomUUID()))
        );
    }

    public static Stream<Arguments> provideInvalidUserRequests() {
        return Stream.of(
                Arguments.of(new UserRequestDTO(null, "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Jo", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("J".repeat(51), "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", null, "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "J", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "J".repeat(51), "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", null, "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "D", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "D".repeat(51), "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", null, "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "invalid-email", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", null, "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "short1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "long".repeat(6) + "1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "PASSWORD1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1", "ROLE_USER", LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", null, LocalDate.of(2000, 1, 1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", null, "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.now().plusDays(1), "+79991112233")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), null)),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "1234567890")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+7123456789")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+712345678901")),
                Arguments.of(new UserRequestDTO("Username", "John", "Doe", "john.doe@example.com", "Password1!", "ROLE_USER", LocalDate.of(2000, 1, 1), "+7abcdefghij"))
        );
    }

    public static Stream<Arguments> provideInvalidLoginRequests() {
        return Stream.of(
                Arguments.of(new LoginRequestDTO("", "ValidP@ssw0rd")),
                Arguments.of(new LoginRequestDTO("not-an-email", "ValidP@ssw0rd")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", null)),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "short1!")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "long".repeat(6) + "1!")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "password1!")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "PASSWORD1!")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "Password!")),
                Arguments.of(new LoginRequestDTO("john.doe@example.com", "Password1"))

        );
    }

    public static Stream<Arguments> provideFilters() {
        Task task = createTask(TaskStatus.COMPLETED, TaskPriority.LOW);
        final List<Task> tasks = Arrays.asList(task, task);
        final List<Task> emptyTasks = Collections.emptyList();

        return Stream.of(
                Arguments.of(createTaskFilterDTO(null, null, null, null), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, null, null, null), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(null, ASSIGNEE_ID_EXAMPLE, null, null), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(null, null, TASK_STATUS_EXAMPLE_ENUM, null), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(null, null, null, TASK_PRIORITY_EXAMPLE_ENUM), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, ASSIGNEE_ID_EXAMPLE, null, null), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, null, TASK_STATUS_EXAMPLE_ENUM, null), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, null, null, TASK_PRIORITY_EXAMPLE_ENUM), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(null, ASSIGNEE_ID_EXAMPLE, TASK_STATUS_EXAMPLE_ENUM, null), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(null, ASSIGNEE_ID_EXAMPLE, null, TASK_PRIORITY_EXAMPLE_ENUM), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(null, null, TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, ASSIGNEE_ID_EXAMPLE, TASK_STATUS_EXAMPLE_ENUM, null), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, ASSIGNEE_ID_EXAMPLE, null, TASK_PRIORITY_EXAMPLE_ENUM), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, null, TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(null, ASSIGNEE_ID_EXAMPLE, TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(), tasks, tasks.size()),
                Arguments.of(createTaskFilterDTO(AUTHOR_ID_EXAMPLE, ASSIGNEE_ID_EXAMPLE, TASK_STATUS_EXAMPLE_ENUM, TASK_PRIORITY_EXAMPLE_ENUM), emptyTasks, 0)
        );
    }
}
