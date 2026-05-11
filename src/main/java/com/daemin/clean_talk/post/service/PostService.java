package com.daemin.clean_talk.post.service;

import com.daemin.clean_talk.domain.Post;
import com.daemin.clean_talk.domain.User;
import com.daemin.clean_talk.post.dto.PostCreateRequest;
import com.daemin.clean_talk.post.dto.PostResponse;
import com.daemin.clean_talk.post.repository.PostRepository;
import com.daemin.clean_talk.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse createPost(PostCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = Post.create(user, request.content());
        Post savedPost = postRepository.save(post);

        return toResponse(savedPost);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return toResponse(post);
    }

    private PostResponse toResponse(Post post) {
        User user = post.getUser();

        return new PostResponse(
                post.getId(),
                user.getId(),
                user.getNickname(),
                post.getContent(),
                post.getCreatedAt()
        );
    }
}
