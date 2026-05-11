package com.daemin.clean_talk.comment.dto;

public record CommentCreateRequest(
        Long userId,
        String content
) {
}
