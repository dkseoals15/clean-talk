package com.daemin.clean_talk.user.dto;

public record UserCreateRequest(
        String email,
        String password,
        String nickname
) {
}
