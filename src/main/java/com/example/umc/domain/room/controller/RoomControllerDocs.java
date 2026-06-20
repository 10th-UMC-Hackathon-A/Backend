package com.example.umc.domain.room.controller;

import com.example.umc.domain.room.dto.request.ParticipateRoomReqDto;
import com.example.umc.domain.room.dto.request.RoomReqDto;
import com.example.umc.domain.room.dto.request.VoteReqDto;
import com.example.umc.domain.room.dto.response.ParticipantResDto;
import com.example.umc.domain.room.dto.response.RoomResDto;
import com.example.umc.domain.room.dto.response.VoteStatusResDto;
import com.example.umc.domain.room.dto.response.VoteStatusWithAliasResDto;
import com.example.umc.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Room", description = "투표 방 및 투표 관련 API")
public interface RoomControllerDocs {

    @Operation(summary = "투표 방 생성", description = "투표 방 이름을 받아 새로운 투표 방을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 방 생성 성공")
    })
    BaseResponse<RoomResDto> createRoom(
            @RequestBody(description = "투표 방 생성 요청 정보", required = true)
            RoomReqDto request
    );

    @Operation(summary = "투표 방 목록 조회", description = "현재 등록된 투표 방 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 방 목록 조회 성공")
    })
    BaseResponse<List<RoomResDto>> getRoomList();

    @Operation(summary = "투표 방 수정", description = "투표 방 ID와 수정할 이름을 받아 투표 방 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 방 수정 성공")
    })
    BaseResponse<RoomResDto> updateRoom(
            @Parameter(name = "roomId", description = "수정할 투표 방 ID", in = ParameterIn.PATH, required = true)
            Long roomId,
            @RequestBody(description = "투표 방 수정 요청 정보", required = true)
            RoomReqDto request
    );

    @Operation(summary = "투표 방 삭제", description = "투표 방 ID에 해당하는 투표 방을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 방 삭제 성공")
    })
    BaseResponse<String> deleteRoom(
            @Parameter(name = "roomId", description = "삭제할 투표 방 ID", in = ParameterIn.PATH, required = true)
            Long roomId
    );

    @Operation(summary = "투표 방 참여", description = "닉네임과 투표 방 ID를 받아 투표 방에 참여하고, 응답 body로 accessToken을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 방 참여 성공")
    })
    BaseResponse<ParticipantResDto> createParticipant(
            @RequestBody(description = "투표 방 참여 요청 정보", required = true)
            ParticipateRoomReqDto request
    );

    @Operation(
            summary = "투표 생성",
            description = "Authorization 헤더의 Bearer accessToken을 검증한 뒤, 투표 방 ID와 투표 위치 정보를 받아 투표를 생성하고 현재 투표 현황을 반환합니다.",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 생성 성공")
    })
    BaseResponse<List<VoteStatusResDto>> createVote(
            @Parameter(hidden = true)
            String authorizationHeader,
            @RequestBody(description = "투표 생성 요청 정보", required = true)
            VoteReqDto request
    ) throws Exception;

    @Operation(summary = "투표 현황 조회", description = "투표 방 ID에 해당하는 투표 항목별 집계 현황을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "투표 현황 조회 성공")
    })
    BaseResponse<List<VoteStatusResDto>> getVoteStatus(
            @Parameter(name = "roomId", description = "조회할 투표 방 ID", in = ParameterIn.QUERY, required = true)
            Long roomId
    );

    @Operation(summary = "참여자 포함 투표 현황 조회", description = "투표 방 ID에 해당하는 투표 항목별 참여자 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "참여자 포함 투표 현황 조회 성공")
    })
    BaseResponse<List<VoteStatusWithAliasResDto>> getVoteStatusWithMember(
            @Parameter(name = "roomId", description = "조회할 투표 방 ID", in = ParameterIn.QUERY, required = true)
            Long roomId
    );
}
