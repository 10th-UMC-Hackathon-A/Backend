package com.example.umc.domain.penalty.dto.response;

import com.example.umc.domain.penalty.enums.GameTypeEnum;

import java.util.List;

public record PenaltyUserDrawResultResDto(
        Long roomId,
        Integer drawRound,
        String nickName,
        GameTypeEnum penaltyType,
        Integer winnerIndex,
        List<String> drawUserList
) {
}
