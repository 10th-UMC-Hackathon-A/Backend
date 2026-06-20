package com.example.umc.domain.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VoteReqDto(
        @NotNull(message = "roomId는 필수입니다.")
        @Positive(message = "roomId는 1 이상이어야 합니다.")
        Long roomId,

        @NotBlank(message = "투표 선택지는 필수입니다.")
        String position
) {
}
