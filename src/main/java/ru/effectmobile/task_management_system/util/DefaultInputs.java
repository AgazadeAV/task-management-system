package ru.effectmobile.task_management_system.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultInputs {
    public static final String TASK_ID_EXAMPLE_JSON = "550e8400-e29b-41d4-a716-446655440000";
    public static final String AUTHOR_ID_EXAMPLE_JSON = "660e8500-f39c-42f4-b827-557766551111";
    public static final String ASSIGNEE_ID_EXAMPLE_JSON = "770e8600-f49d-43e5-c938-668877662222";
    public static final String USER_ID_EXAMPLE_JSON = "880e8700-e29b-41d4-a716-446655440000";
    public static final String COMMENT_ID_EXAMPLE_JSON = "780f3c00-a79c-45b8-9d75-33f3a9cd4312";
    public static final UUID TASK_ID_EXAMPLE = UUID.fromString(TASK_ID_EXAMPLE_JSON);
    public static final UUID AUTHOR_ID_EXAMPLE = UUID.fromString(AUTHOR_ID_EXAMPLE_JSON);
    public static final UUID ASSIGNEE_ID_EXAMPLE = UUID.fromString(ASSIGNEE_ID_EXAMPLE_JSON);
    public static final UUID USER_ID_EXAMPLE = UUID.fromString(ASSIGNEE_ID_EXAMPLE_JSON);
    public static final UUID COMMENT_ID_EXAMPLE = UUID.fromString(COMMENT_ID_EXAMPLE_JSON);
    public static final String TASK_TITLE_EXAMPLE = "Implement login feature";
    public static final String TASK_DESCRIPTION_EXAMPLE = "Develop a user authentication system";
    public static final String TASK_STATUS_EXAMPLE = "IN_PROGRESS";
    public static final String TASK_PRIORITY_EXAMPLE = "HIGH";
    public static final String COMMENT_TEXT_EXAMPLE = "This feature needs further testing.";
    public static final String USERNAME_EXAMPLE = "john_doe";
    public static final String FIRST_NAME_EXAMPLE = "John";
    public static final String LAST_NAME_EXAMPLE = "Doe";
    public static final String EMAIL_EXAMPLE = "john.doe@example.com";
    public static final String PASSWORD_EXAMPLE = "P@ssw0rd";
    public static final String ROLE_EXAMPLE = "USER";
    public static final String BIRTH_DATE_EXAMPLE = "1990-05-15";
    public static final String PHONE_NUMBER_EXAMPLE = "+71234567890";
    public static final String DATE_EXAMPLE = "2025-03-14T12:30:45";
}
