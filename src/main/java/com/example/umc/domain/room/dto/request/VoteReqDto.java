package com.example.umc.domain.room.dto.request;

public record VoteReqDto(
        Long roomId,
        //TODO- VoteType을 만든 이유가 확장성 떄문이였는데, Enum을 쓰면 Vote Type을 만든 이유가 없어지는거 같음. ..
        String position
) {
}
