package com.daemin.clean_talk.moderation.service;

import com.daemin.clean_talk.moderation.client.OpenAiModerationClient;
import com.daemin.clean_talk.moderation.dto.CommentCheckRequest;
import com.daemin.clean_talk.moderation.dto.CommentCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final OpenAiModerationClient openAiModerationClient;

    public CommentCheckResponse checkComment(CommentCheckRequest request) {
        return openAiModerationClient.check(request.content());
    }
}
