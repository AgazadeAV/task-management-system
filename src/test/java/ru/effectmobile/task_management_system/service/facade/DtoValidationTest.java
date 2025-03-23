package ru.effectmobile.task_management_system.service.facade;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.effectmobile.task_management_system.dto.requests.CommentRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.LoginRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.TaskRequestDTO;
import ru.effectmobile.task_management_system.dto.requests.UserRequestDTO;

import static org.junit.Assert.assertThrows;
import static ru.effectmobile.task_management_system.util.DefaultInputs.EMAIL_EXAMPLE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class DtoValidationTest {

    @Autowired
    private CommentFacade commentFacade;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private UserFacade userFacade;

    @ParameterizedTest
    @MethodSource("ru.effectmobile.task_management_system.util.ModelCreator#provideInvalidCommentRequests")
    void createComment_ShouldThrowConstraintViolationException_WhenDtoIsInvalid(CommentRequestDTO invalidDto) {
        assertThrows(ConstraintViolationException.class, () -> commentFacade.createComment(invalidDto, EMAIL_EXAMPLE));
    }

    @ParameterizedTest
    @MethodSource("ru.effectmobile.task_management_system.util.ModelCreator#provideInvalidTaskRequests")
    void createTask_ShouldThrowConstraintViolationException_WhenDtoIsInvalid(TaskRequestDTO invalidDto) {
        assertThrows(ConstraintViolationException.class, () -> taskFacade.createTask(invalidDto, EMAIL_EXAMPLE));
    }

    @ParameterizedTest
    @MethodSource("ru.effectmobile.task_management_system.util.ModelCreator#provideInvalidUserRequests")
    void createUser_ShouldThrowConstraintViolationException_WhenDtoIsInvalid(UserRequestDTO invalidDto) {
        assertThrows(ConstraintViolationException.class, () -> userFacade.createUser(invalidDto));
    }

    @ParameterizedTest
    @MethodSource("ru.effectmobile.task_management_system.util.ModelCreator#provideInvalidLoginRequests")
    void login_ShouldThrowConstraintViolationException_WhenDtoIsInvalid(LoginRequestDTO invalidDto) {
        assertThrows(ConstraintViolationException.class, () -> userFacade.login(invalidDto));
    }
}
