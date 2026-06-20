package com.example.umc.domain.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VoteTypeReqDto(
        @NotBlank(message = "투표 선택지 이름은 필수입니다.")
        @Size(max = 50, message = "투표 선택지 이름은 50자 이하여야 합니다.")
        String label
) {
}
