package com.daemin.clean_talk.moderation.controller;

import com.daemin.clean_talk.moderation.dto.CommentCheckRequest;
import com.daemin.clean_talk.moderation.dto.CommentCheckResponse;
import com.daemin.clean_talk.moderation.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;

    @PostMapping("/check")
    public CommentCheckResponse checkComment(@RequestBody CommentCheckRequest request) {
        return moderationService.checkComment(request);
    }
}
