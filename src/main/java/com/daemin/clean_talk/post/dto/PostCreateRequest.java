package com.daemin.clean_talk.post.dto;

public record PostCreateRequest(
        Long userId,
        String content
) {
}
