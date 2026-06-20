package com.example.umc.domain.penalty.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PenaltyDrawResultResDto(
        Long roomId,
        Integer drawRound,
        String label,
        Integer prizeIndex,
        List<String> penaltyList
) {}
