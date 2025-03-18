package ru.effectmobile.task_management_system.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.effectmobile.task_management_system.repository.CommentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentSecurityService {

    private final CommentRepository commentRepository;

    public boolean isCommentOwner(UUID commentId, String username) {
        return commentRepository.findById(commentId)
                .map(comment -> comment.getAuthor().getEmail().equals(username))
                .orElse(false);
    }
}
