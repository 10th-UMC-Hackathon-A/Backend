package com.example.umc.domain.room.service;

import com.example.umc.domain.room.dto.request.ParticipateRoomReqDto;
import com.example.umc.domain.room.dto.request.RoomReqDto;
import com.example.umc.domain.room.dto.request.VoteReqDto;
import com.example.umc.domain.room.dto.response.RoomResDto;
import com.example.umc.domain.room.dto.response.VoteStatusResDto;
import com.example.umc.domain.room.dto.response.VoteStatusWithAliasResDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    public RoomResDto createRoom(RoomReqDto request) {
        return new RoomResDto(request.roomName(), 1L);
    }

    public List<RoomResDto> getRooms() {
        return List.of(
                new RoomResDto("AI 공학관 502호 강의실", 1L),
                new RoomResDto("AI 공학관 503호 강의실", 2L)
        );
    }

    public RoomResDto updateRoom(Long roomId, RoomReqDto request) {
        return new RoomResDto(request.roomName(), roomId);
    }

    public void deleteRoom(Long roomId) {
    }

    public void participateRoom(ParticipateRoomReqDto request) {
    }

    public List<VoteStatusResDto> vote(VoteReqDto request) {
        return getVoteStatus(request.roomId());
    }

    public List<VoteStatusResDto> getVoteStatus(Long roomId) {
        return List.of(
                new VoteStatusResDto("추워요", 3),
                new VoteStatusResDto("더워요", 2)
        );
    }

    public List<VoteStatusWithAliasResDto> getVoteStatusWithMembers(Long roomId) {
        return List.of(
                new VoteStatusWithAliasResDto("추워요", List.of("추워요1", "추워요2", "추워요3")),
                new VoteStatusWithAliasResDto("더워요", List.of("더워요1", "더워요2"))
        );
    }
}
