package com.example.umc.domain.room.dto.response;

import java.time.LocalDateTime;

public record RoomDetailsResDto(
        Long roomId,
        String roomName,
        LocalDateTime voteStartedAt,
        LocalDateTime voteClosedAt,
        Integer drawRound,
        Integer participantedUserCount
) { }
