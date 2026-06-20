package com.example.umc.domain.room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoomReqDto(
        @NotBlank(message = "방 이름은 필수입니다.")
        @Size(max = 100, message = "방 이름은 100자 이하여야 합니다.")
        String roomName
) {
}
