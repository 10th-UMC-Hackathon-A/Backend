package com.example.umc.domain.room.controller;

import com.example.umc.domain.room.dto.request.ParticipateRoomReqDto;
import com.example.umc.domain.room.dto.request.RoomReqDto;
import com.example.umc.domain.room.dto.request.VoteTypeReqDto;
import com.example.umc.domain.room.dto.request.VoteReqDto;
import com.example.umc.domain.room.dto.response.*;
import com.example.umc.domain.room.service.RoomService;
import com.example.umc.global.common.base.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController implements RoomControllerDocs {

    private final RoomService roomService;

    @PostMapping
    public BaseResponse<RoomResDto> createRoom(
            @Valid
            @RequestBody
            RoomReqDto request
    ) {
        return BaseResponse.onSuccess(roomService.createRoom(request));
    }

    @GetMapping
    public BaseResponse<List<RoomResDto>> getRoomList() {
        return BaseResponse.onSuccess(roomService.getRooms());
    }

    @GetMapping("/details")
    public BaseResponse<RoomDetailsResDto> getRoomDetails(
            @RequestParam
            Long roomId
    ) {
        return BaseResponse.onSuccess(roomService.getRoomDetails(roomId));
    }

    @PostMapping("/vote-types")
    public BaseResponse<VoteTypeResDto> createVoteType(
            @Valid
            @RequestBody
            VoteTypeReqDto request
    ) {
        return BaseResponse.onSuccess(roomService.createVoteType(request));
    }

    @GetMapping("/vote-types")
    public BaseResponse<List<VoteTypeResDto>> getVoteTypeList() {
        return BaseResponse.onSuccess(roomService.getVoteTypes());
    }

    @PatchMapping("/{roomId}")
    public BaseResponse<RoomResDto> updateRoom(
            @PathVariable
            Long roomId,
            @Valid
            @RequestBody
            RoomReqDto request
    ) {
        RoomResDto result = roomService.updateRoom(roomId, request);
        return BaseResponse.onSuccess(result);
    }

    @DeleteMapping("/{roomId}")
    public BaseResponse<String> deleteRoom(
            @PathVariable
            Long roomId
    ) {
        roomService.deleteRoom(roomId);
        return BaseResponse.onSuccess("투표 방 삭제가 완료되었습니다.");
    }

    @PostMapping("/participants")
    public BaseResponse<ParticipantResDto> createParticipant(
            @RequestBody
            ParticipateRoomReqDto request
    ) {
        return BaseResponse.onSuccess(roomService.participateRoom(request));
    }

    @GetMapping("/{roomId}/participants")
    public BaseResponse<ParticipantNickNameResDto> createParticipant(
            @RequestHeader("Authorization")
            String authorizationHeader,
            @PathVariable
            Long roomId
    ) {
        return BaseResponse.onSuccess(roomService.getParticipantNickName(authorizationHeader, roomId));
    }

    @PostMapping("/vote")
    public BaseResponse<List<VoteStatusResDto>> createVote(
            @RequestHeader("Authorization")
            String authorizationHeader,
            @Valid
            @RequestBody
            VoteReqDto request
    ) throws Exception {
        return BaseResponse.onSuccess(roomService.vote(authorizationHeader, request));
    }

    @GetMapping("/vote")
    public BaseResponse<List<VoteStatusResDto>> getVoteStatus(
            @RequestParam
            Long roomId
    ) {
        return BaseResponse.onSuccess(roomService.getVoteStatus(roomId));
    }

    @GetMapping("/vote-members")
    public BaseResponse<List<VoteStatusWithAliasResDto>> getVoteStatusWithMember(
            @RequestParam
            Long roomId
    ) {
        return BaseResponse.onSuccess(roomService.getVoteStatusWithMembers(roomId));
    }
}
