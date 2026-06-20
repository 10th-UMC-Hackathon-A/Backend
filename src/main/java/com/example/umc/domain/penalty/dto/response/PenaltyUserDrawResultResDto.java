package com.example.umc.domain.penalty.dto.response;

import java.util.List;

public record PenaltyUserDrawResultResDto(
        Long roomId,
        Integer drawRound,
        String nickName,
        Integer winnerIndex,
        List<String> drawUserList
) {
}
