package com.example.umc.domain.room.dto.response;

public record ParticipantResDto(
        Long userId,
        Long roomId,
        String nickName,
        String uid,
        String accessToken
) {
}
