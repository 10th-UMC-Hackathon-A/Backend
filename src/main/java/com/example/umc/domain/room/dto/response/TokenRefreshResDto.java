package com.example.umc.domain.room.dto.response;

public record TokenRefreshResDto(
        String accessToken,
        String refreshToken
) {
}
