package ru.effectmobile.task_management_system.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.effectmobile.task_management_system.exception.custom.notfound.CommentNotFoundException;
import ru.effectmobile.task_management_system.model.entity.Comment;
import ru.effectmobile.task_management_system.model.entity.Task;
import ru.effectmobile.task_management_system.model.enums.TaskPriority;
import ru.effectmobile.task_management_system.model.enums.TaskStatus;
import ru.effectmobile.task_management_system.repository.CommentRepository;
import ru.effectmobile.task_management_system.service.impl.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.effectmobile.task_management_system.util.ModelCreator.createComment;
import static ru.effectmobile.task_management_system.util.ModelCreator.createTask;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    private static final PageRequest PAGEABLE = PageRequest.of(0, 10);
    private static final Task TASK = createTask(TaskStatus.COMPLETED, TaskPriority.LOW);
    private static final Comment COMMENT = createComment(TASK);

    @Test
    void findAll_ShouldReturnPageOfComments() {
        Page<Comment> page = new PageImpl<>(List.of(COMMENT));
        when(commentRepository.findAll(PAGEABLE)).thenReturn(page);

        Page<Comment> result = commentService.findAll(PAGEABLE);

        assertEquals(1, result.getTotalElements());
        verify(commentRepository).findAll(PAGEABLE);
    }

    @Test
    void findById_ShouldReturnComment_WhenExists() {
        when(commentRepository.findById(COMMENT.getId())).thenReturn(Optional.of(COMMENT));

        Comment result = commentService.findById(COMMENT.getId());

        assertEquals(COMMENT.getId(), result.getId());
        verify(commentRepository).findById(COMMENT.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(commentRepository.findById(COMMENT.getId())).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.findById(COMMENT.getId()));
        verify(commentRepository).findById(COMMENT.getId());
    }

    @Test
    void save_ShouldReturnSavedComment() {
        when(commentRepository.save(COMMENT)).thenReturn(COMMENT);

        Comment result = commentService.save(COMMENT);

        assertEquals(COMMENT, result);
        verify(commentRepository).save(COMMENT);
    }

    @Test
    void deleteById_ShouldDeleteComment_WhenExists() {
        when(commentRepository.findById(COMMENT.getId())).thenReturn(Optional.of(COMMENT));
        doNothing().when(commentRepository).delete(COMMENT);

        assertDoesNotThrow(() -> commentService.deleteById(COMMENT.getId()));
        verify(commentRepository).findById(COMMENT.getId());
        verify(commentRepository).delete(COMMENT);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(commentRepository.findById(COMMENT.getId())).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteById(COMMENT.getId()));
        verify(commentRepository).findById(COMMENT.getId());
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void findByTaskId_ShouldReturnPageOfComments() {
        Page<Comment> page = new PageImpl<>(List.of(COMMENT));
        when(commentRepository.findByTaskId(TASK.getId(), PAGEABLE)).thenReturn(page);

        Page<Comment> result = commentService.findByTaskId(TASK.getId(), PAGEABLE);

        assertEquals(1, result.getTotalElements());
        verify(commentRepository).findByTaskId(TASK.getId(), PAGEABLE);
    }

    @Test
    void findByTaskId_ShouldReturnEmptyPage_WhenNoComments() {
        Page<Comment> page = new PageImpl<>(List.of());
        when(commentRepository.findByTaskId(TASK.getId(), PAGEABLE)).thenReturn(page);

        Page<Comment> result = commentService.findByTaskId(TASK.getId(), PAGEABLE);

        assertTrue(result.isEmpty());
        verify(commentRepository).findByTaskId(TASK.getId(), PAGEABLE);
    }
}
