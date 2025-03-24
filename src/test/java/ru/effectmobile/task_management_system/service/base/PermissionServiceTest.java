package ru.effectmobile.task_management_system.service.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.effectmobile.task_management_system.exception.custom.auth.UserDoesntHaveEnoughRightsException;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.service.impl.PermissionServiceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAsigneeUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createAuthorUser;
import static ru.effectmobile.task_management_system.util.ModelCreator.createComment;
import static ru.effectmobile.task_management_system.util.ModelCreator.createUser;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @InjectMocks
    private PermissionServiceImpl permissionService;

    private static final User ADMIN = createAuthorUser(Role.ROLE_ADMIN);
    private static final User USER = createAsigneeUser(Role.ROLE_USER);
    private static final User OTHER_USER = createUser(Role.ROLE_TEST);
    private static final Comment ADMIN_COMMENT = createComment(ADMIN);
    private static final Comment USER_COMMENT = createComment(USER);


    @Test
    void checkCanDeleteComment_ShouldPass_ForAdmin() {
        assertDoesNotThrow(() -> permissionService.checkCanDeleteComment(ADMIN_COMMENT, ADMIN));
    }

    @Test
    void checkCanDeleteComment_ShouldPass_ForAuthor() {
        assertDoesNotThrow(() -> permissionService.checkCanDeleteComment(USER_COMMENT, USER));
    }

    @Test
    void checkCanDeleteComment_ShouldThrow_ForOtherUser() {
        assertThrows(UserDoesntHaveEnoughRightsException.class, () ->
                permissionService.checkCanDeleteComment(USER_COMMENT, OTHER_USER));
    }

    @Test
    void checkCanCreateTask_ShouldPass_ForAdmin() {
        assertDoesNotThrow(() -> permissionService.checkCanCreateTask(ADMIN));
    }

    @Test
    void checkCanCreateTask_ShouldThrow_ForNonAdmin() {
        assertThrows(UserDoesntHaveEnoughRightsException.class, () ->
                permissionService.checkCanCreateTask(USER));
    }

    @Test
    void checkCanDeleteTask_ShouldPass_ForAdmin() {
        assertDoesNotThrow(() -> permissionService.checkCanDeleteTask(ADMIN));
    }

    @Test
    void checkCanDeleteTask_ShouldThrow_ForNonAdmin() {
        assertThrows(UserDoesntHaveEnoughRightsException.class, () ->
                permissionService.checkCanDeleteTask(USER));
    }

    @Test
    void checkCanUpdateTask_ShouldPass_ForAdmin() {
        assertDoesNotThrow(() -> permissionService.checkCanUpdateTask(ADMIN, ADMIN.getId()));
    }

    @Test
    void checkCanUpdateTask_ShouldPass_ForSelfAssignee() {
        assertDoesNotThrow(() -> permissionService.checkCanUpdateTask(USER, USER.getId()));
    }

    @Test
    void checkCanUpdateTask_ShouldThrow_ForUnrelatedUser() {
        assertThrows(UserDoesntHaveEnoughRightsException.class, () ->
                permissionService.checkCanUpdateTask(USER, OTHER_USER.getId()));
    }
}
