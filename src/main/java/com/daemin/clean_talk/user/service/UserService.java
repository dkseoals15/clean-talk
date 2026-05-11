package com.daemin.clean_talk.user.service;

import com.daemin.clean_talk.domain.User;
import com.daemin.clean_talk.user.dto.UserCreateRequest;
import com.daemin.clean_talk.user.dto.UserResponse;
import com.daemin.clean_talk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse signup(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // TODO: 비밀번호 암호화 적용
        User user = User.create(
                request.email(),
                request.password(),
                request.nickname()
        );

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getNickname(),
                savedUser.getCreatedAt()
        );
    }
}
