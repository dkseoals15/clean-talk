package com.daemin.clean_talk.comment.service;

import com.daemin.clean_talk.comment.dto.CommentCreateRequest;
import com.daemin.clean_talk.comment.dto.CommentResponse;
import com.daemin.clean_talk.comment.repository.CommentRepository;
import com.daemin.clean_talk.domain.Comment;
import com.daemin.clean_talk.domain.CommentStatus;
import com.daemin.clean_talk.domain.ModerationResult;
import com.daemin.clean_talk.domain.Post;
import com.daemin.clean_talk.domain.User;
import com.daemin.clean_talk.moderation.dto.CommentCheckRequest;
import com.daemin.clean_talk.moderation.dto.CommentCheckResponse;
import com.daemin.clean_talk.moderation.repository.ModerationResultRepository;
import com.daemin.clean_talk.moderation.service.ModerationService;
import com.daemin.clean_talk.post.repository.PostRepository;
import com.daemin.clean_talk.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModerationService moderationService;
    private final ModerationResultRepository moderationResultRepository;

    @Transactional
    public CommentResponse createComment(Long postId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        CommentCheckResponse checkResponse = moderationService.checkComment(
                new CommentCheckRequest(request.content())
        );
        boolean toxic = Boolean.TRUE.equals(checkResponse.toxic());
        String savedContent = toxic ? checkResponse.refinedContent() : request.content();
        CommentStatus status = toxic ? CommentStatus.REFINED : CommentStatus.APPROVED;

        Comment comment = Comment.create(
                post,
                user,
                request.content(),
                savedContent,
                status
        );
        Comment savedComment = commentRepository.save(comment);

        ModerationResult moderationResult = ModerationResult.create(
                savedComment,
                toxic,
                checkResponse.reason(),
                checkResponse.refinedContent()
        );
        moderationResultRepository.save(moderationResult);

        return toResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CommentResponse toResponse(Comment comment) {
        Post post = comment.getPost();
        User user = comment.getUser();

        return new CommentResponse(
                comment.getId(),
                post.getId(),
                user.getId(),
                user.getNickname(),
                comment.getOriginalContent(),
                comment.getContent(),
                comment.getStatus(),
                comment.getCreatedAt()
        );
    }
}
