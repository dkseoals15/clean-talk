package com.daemin.clean_talk.moderation.service;

import com.daemin.clean_talk.moderation.dto.CommentCheckRequest;
import com.daemin.clean_talk.moderation.dto.CommentCheckResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ModerationService {

    private static final List<String> TOXIC_KEYWORDS = List.of("멍청", "바보", "싫어", "꺼져");
    private static final String TOXIC_REASON = "상대방을 공격하거나 부정적인 표현이 포함되어 있습니다.";
    private static final String REFINED_CONTENT = "조금 더 부드럽게 표현해보면 좋겠습니다.";

    public CommentCheckResponse checkComment(CommentCheckRequest request) {
        String content = request.content();
        boolean toxic = content != null && TOXIC_KEYWORDS.stream().anyMatch(content::contains);

        if (!toxic) {
            return new CommentCheckResponse(false, null, null);
        }

        return new CommentCheckResponse(true, TOXIC_REASON, REFINED_CONTENT);
    }
}
