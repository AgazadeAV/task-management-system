package ru.effectmobile.task_management_system.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.effectmobile.task_management_system.repository.CommentRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentSecurityService {

    private final CommentRepository commentRepository;

    public boolean isCommentOwner(UUID commentId, String username) {
        log.debug("Checking if user '{}' is the owner of comment '{}'", username, commentId);

        return commentRepository.findById(commentId)
                .map(comment -> {
                    boolean isOwner = comment.getAuthor().getEmail().equals(username);
                    log.debug("Comment found. Owner check: {}", isOwner);
                    return isOwner;
                })
                .orElseGet(() -> {
                    log.warn("Comment with ID '{}' not found.", commentId);
                    return false;
                });
    }
}
