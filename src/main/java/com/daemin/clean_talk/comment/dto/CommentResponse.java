package com.daemin.clean_talk.comment.dto;

import com.daemin.clean_talk.domain.CommentStatus;
import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long postId,
        Long userId,
        String nickname,
        String originalContent,
        String content,
        CommentStatus status,
        LocalDateTime createdAt
) {
}
