package com.daemin.clean_talk.post.dto;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long userId,
        String nickname,
        String content,
        LocalDateTime createdAt
) {
}
