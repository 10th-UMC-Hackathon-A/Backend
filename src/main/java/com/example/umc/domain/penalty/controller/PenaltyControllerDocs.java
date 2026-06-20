package com.example.umc.domain.penalty.controller;

import com.example.umc.domain.penalty.dto.request.PenaltyReqDto;
import com.example.umc.domain.penalty.dto.response.PenaltyDrawResultResDto;
import com.example.umc.domain.penalty.dto.response.PenaltyResDto;
import com.example.umc.domain.penalty.dto.response.PenaltyUserDrawResultResDto;
import com.example.umc.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Penalty", description = "벌칙 관리 API")
public interface PenaltyControllerDocs {

    @Operation(summary = "벌칙 생성", description = "벌칙 라벨을 받아 새로운 벌칙을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "벌칙 생성 성공")
    })
    BaseResponse<PenaltyResDto> createPenalty(
            @RequestBody(description = "벌칙 생성 요청 정보", required = true)
            PenaltyReqDto request
    );

    @Operation(summary = "벌칙 목록 조회", description = "현재 등록된 벌칙 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "벌칙 목록 조회 성공")
    })
    BaseResponse<List<PenaltyResDto>> getPenaltyList();

    @Operation(summary = "벌칙자 추첨", description = "투표 결과를 바탕으로 벌칙자를 추첨합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "벌칙자 추첨 성공")
    })
    BaseResponse<PenaltyUserDrawResultResDto> drawPenaltyUser(
            @Parameter(name = "roomId", description = "추첨을 진행할 투표 방 ID", in = ParameterIn.QUERY, required = true)
            Long roomId
    );

    @Operation(summary = "벌칙 추첨", description = "등록된 벌칙 중 하나를 추첨합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "벌칙 추첨 성공")
    })
    BaseResponse<PenaltyDrawResultResDto> drawPenalty(
            @Parameter(name = "roomId", description = "추첨을 진행할 투표 방 ID", in = ParameterIn.QUERY, required = true)
            Long roomId
    );

    @Operation(summary = "벌칙 수정", description = "벌칙 ID와 수정할 라벨을 받아 벌칙 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "벌칙 수정 성공")
    })
    BaseResponse<PenaltyResDto> updatePenalty(
            @Parameter(name = "penaltyId", description = "수정할 벌칙 ID", in = ParameterIn.QUERY, required = true)
            Long penaltyId,
            @RequestBody(description = "벌칙 수정 요청 정보", required = true)
            PenaltyReqDto request
    );

    @Operation(summary = "벌칙 삭제", description = "벌칙 ID에 해당하는 벌칙을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "벌칙 삭제 성공")
    })
    BaseResponse<String> deletePenalty(
            @Parameter(name = "penaltyId", description = "삭제할 벌칙 ID", in = ParameterIn.PATH, required = true)
            Long penaltyId
    );
}
