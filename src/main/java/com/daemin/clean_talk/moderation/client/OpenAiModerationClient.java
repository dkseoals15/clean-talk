package com.daemin.clean_talk.moderation.client;

import com.daemin.clean_talk.moderation.dto.CommentCheckResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenAiModerationClient {

    private static final String MODEL = "gpt-4o-mini";
    private static final String SYSTEM_PROMPT = """
            너는 SNS 댓글을 검사하고 순화하는 AI다.
            비속어, 인신공격, 조롱, 혐오, 위협 표현이 있으면 toxic=true로 판단한다.
            악성 댓글이면 원래 의미를 최대한 유지하되 부드럽고 예의 있는 표현으로 바꾼다.
            정상 댓글이면 toxic=false, reason=null, refinedContent=null로 응답한다.
            반드시 JSON 형식으로만 응답한다.
            마크다운 코드블록은 사용하지 않는다.
            응답 JSON 필드는 toxic, reason, refinedContent만 사용한다.
            """;

    private final String apiKey;
    private final ObjectMapper objectMapper;

    public OpenAiModerationClient(
            @Value("${openai.api-key:}") String apiKey,
            ObjectMapper objectMapper
    ) {
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
    }

    public CommentCheckResponse check(String content) {
        try {
            OpenAIClient client = OpenAIOkHttpClient.builder()
                    .apiKey(resolveApiKey())
                    .build();
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model(MODEL)
                    .addSystemMessage(SYSTEM_PROMPT)
                    .addUserMessage(buildUserPrompt(content))
                    .build();

            ChatCompletion completion = client.chat().completions().create(params);
            String responseContent = completion.choices()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("OpenAI 응답 choice가 비어 있습니다."))
                    .message()
                    .content()
                    .orElseThrow(() -> new IllegalStateException("OpenAI 응답 content가 비어 있습니다."));

            return parseResponse(responseContent);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("OpenAI 댓글 검사 호출에 실패했습니다.", e);
        }
    }

    private String resolveApiKey() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY 환경변수가 설정되어 있지 않습니다.");
        }

        return apiKey;
    }

    private String buildUserPrompt(String content) {
        return """
                다음 SNS 댓글을 검사하고 JSON으로만 응답해줘.

                댓글:
                %s
                """.formatted(content);
    }

    private CommentCheckResponse parseResponse(String responseContent) {
        try {
            return objectMapper.readValue(responseContent, CommentCheckResponse.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("OpenAI 응답 JSON 파싱에 실패했습니다.", e);
        }
    }
}
