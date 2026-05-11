package com.daemin.clean_talk.moderation.dto;

public record CommentCheckResponse(
        Boolean toxic,
        String reason,
        String refinedContent
) {
}
