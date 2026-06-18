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
        return null;
    }

    public List<RoomResDto> getRooms() {
        return null;
    }

    public RoomResDto updateRoom(Long roomId, RoomReqDto request) {
        return null;
    }

    public void deleteRoom(Long roomId) {
    }

    public void participateRoom(ParticipateRoomReqDto request) {
    }

    public List<VoteStatusResDto> vote(VoteReqDto request) {
        return null;
    }

    public List<VoteStatusResDto> getVoteStatus(Long roomId) {
        return null;
    }

    public List<VoteStatusWithAliasResDto> getVoteStatusWithMembers(Long roomId) {
        return null;
    }
}
