package com.example.umc.domain.penalty.dto.response;

import com.example.umc.domain.penalty.enums.GameTypeEnum;
import lombok.Builder;

import java.util.List;

@Builder
public record PenaltyDrawResultResDto(
        Long roomId,
        Integer drawRound,
        String label,
        GameTypeEnum penaltyType,
        Integer prizeIndex,
        List<String> penaltyList
) {}
