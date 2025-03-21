package ru.effectmobile.task_management_system.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultInputs {

    public static final String TASK_ID_EXAMPLE_JSON = "550e8400-e29b-41d4-a716-446655440000";
    public static final String AUTHOR_ID_EXAMPLE_JSON = "660e8500-f39c-42f4-b827-557766551111";
    public static final String ASSIGNEE_ID_EXAMPLE_JSON = "770e8600-f49d-43e5-c938-668877662222";
    public static final String USER_ID_EXAMPLE_JSON = "880e8700-e29b-41d4-a716-446655440000";
    public static final String COMMENT_ID_EXAMPLE_JSON = "780f3c00-a79c-45b8-9d75-33f3a9cd4312";
    public static final UUID TASK_ID_EXAMPLE = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    public static final UUID AUTHOR_ID_EXAMPLE = UUID.fromString("660e8500-f39c-42f4-b827-557766551111");
    public static final UUID ASSIGNEE_ID_EXAMPLE = UUID.fromString("770e8600-f49d-43e5-c938-668877662222");
    public static final UUID USER_ID_EXAMPLE = UUID.fromString("880e8700-e29b-41d4-a716-446655440000");
    public static final UUID COMMENT_ID_EXAMPLE = UUID.fromString("780f3c00-a79c-45b8-9d75-33f3a9cd4312");
    public static final String TASK_TITLE_EXAMPLE = "Implement login feature";
    public static final String TASK_DESCRIPTION_EXAMPLE = "Develop a user authentication system";
    public static final String TASK_STATUS_EXAMPLE = "IN_PROGRESS";
    public static final String TASK_PRIORITY_EXAMPLE = "HIGH";
    public static final String COMMENT_TEXT_EXAMPLE = "This feature needs further testing.";
    public static final String USERNAME_EXAMPLE = "john_doe";
    public static final String FIRST_NAME_EXAMPLE = "John";
    public static final String LAST_NAME_EXAMPLE = "Doe";
    public static final String EMAIL_EXAMPLE = "john.doe@example.com";
    public static final String PASSWORD_EXAMPLE = "securePassword123!";
    public static final String ROLE_EXAMPLE = "ROLE_USER";
    public static final String BIRTH_DATE_EXAMPLE = "1990-05-15";
    public static final String PHONE_NUMBER_EXAMPLE = "+71234567890";
    public static final String DATE_EXAMPLE = "2025-03-14T12:30:45";
    public static final String TOKEN_EXAMPLE =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                    + "eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzEwNDI1NzAwLCJleHAiOjE3MTA0NjE3MDB9."
                    + "hWdpzJm-rKXzGBdVf4fTo5XAxIhTjlO9lzK8poAVXaM";
    public static final int VERSION_EXAMPLE = 1;
    public static final LocalDateTime CREATION_DATE_EXAMPLE = LocalDateTime.now();
    public static final LocalDateTime UPDATE_DATE_EXAMPLE = LocalDateTime.now();
    public static final Role ROLE_EXAMPLE_ENUM = Role.ROLE_USER;
    public static final LocalDate BIRTH_DATE_EXAMPLE_LOCALDATE = LocalDate.parse(BIRTH_DATE_EXAMPLE);
    public static final TaskStatus TASK_STATUS_EXAMPLE_ENUM = TaskStatus.IN_PROGRESS;
    public static final TaskPriority TASK_PRIORITY_EXAMPLE_ENUM = TaskPriority.HIGH;
    public static final String ENCRYPTED_USERNAME_EXAMPLE = "encryptedUsername";
    public static final String ENCRYPTED_EMAIL_EXAMPLE = "encryptedEmail@example.com";
    public static final String ENCRYPTED_PHONE_NUMBER_EXAMPLE = "encryptedPhoneNumber";
}
