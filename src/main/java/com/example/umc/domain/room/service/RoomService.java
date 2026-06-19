package com.example.umc.domain.room.service;

import com.example.umc.domain.room.dto.request.ParticipateRoomReqDto;
import com.example.umc.domain.room.dto.request.RoomReqDto;
import com.example.umc.domain.room.dto.request.VoteReqDto;
import com.example.umc.domain.room.dto.response.RoomResDto;
import com.example.umc.domain.room.dto.response.VoteStatusResDto;
import com.example.umc.domain.room.dto.response.VoteStatusWithAliasResDto;
import com.example.umc.domain.room.entity.Room;
import com.example.umc.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    public RoomResDto createRoom(RoomReqDto request) {
        Room room = toRoom(request);
        Room savedRoom = roomRepository.save(room);
        return toCreateRoomResDto(savedRoom);
    }

    public List<RoomResDto> getRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(this::toRoomResDto)
                .toList();
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

    private RoomResDto toRoomResDto(Room room) {
        return new RoomResDto(room.getRoomName(), room.getRoomId());
    }

    private Room toRoom(RoomReqDto request) {
        return Room.builder()
                .roomName(request.roomName())
                .build();
    }

    private RoomResDto toCreateRoomResDto(Room room) {
        return new RoomResDto(room.getRoomName(), room.getRoomId());
    }
}
