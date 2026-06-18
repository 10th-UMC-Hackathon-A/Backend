package com.example.umc.domain.room.dto.response;

public record VoteStatusResDto(
        String label, // Vote Type Label. e.g. 더워요
        Integer count
) {
}
