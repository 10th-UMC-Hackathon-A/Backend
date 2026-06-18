package com.example.umc.domain.room.dto.response;

import java.util.List;

public record VoteStatusWithAliasResDto(
        String label,
        List<String> participateList
) {
}
