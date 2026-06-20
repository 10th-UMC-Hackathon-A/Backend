package com.example.umc.domain.penalty.controller;

import com.example.umc.domain.penalty.dto.request.PenaltyReqDto;
import com.example.umc.domain.penalty.dto.response.MissionCompleteResDto;
import com.example.umc.domain.penalty.dto.response.PenaltyDrawResultResDto;
import com.example.umc.domain.penalty.dto.response.PenaltyResDto;
import com.example.umc.domain.penalty.dto.response.PenaltyUserDrawResultResDto;
import com.example.umc.domain.penalty.service.PenaltyService;
import com.example.umc.global.common.base.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/penalties")
public class PenaltyController implements PenaltyControllerDocs {

    private final PenaltyService penaltyService;

    @PostMapping
    public BaseResponse<PenaltyResDto> createPenalty(
            @Valid
            @RequestBody
            PenaltyReqDto request
    ) {
        return BaseResponse.onSuccess(penaltyService.createPenalty(request));
    }

    @GetMapping
    public BaseResponse<List<PenaltyResDto>> getPenaltyList() {
        return BaseResponse.onSuccess(penaltyService.getPenalties());
    }

    @PostMapping("/draw-user")
    public BaseResponse<PenaltyUserDrawResultResDto> drawPenaltyUser(
            @RequestParam
            Long roomId
    ) {
        return BaseResponse.onSuccess(penaltyService.drawPenaltyUser(roomId));
    }

    @PostMapping("/draw")
    public BaseResponse<PenaltyDrawResultResDto> drawPenalty(
            @RequestParam
            Long roomId
    ) {
        return BaseResponse.onSuccess(penaltyService.drawPenalty(roomId));
    }

    @PatchMapping("/update")
    public BaseResponse<PenaltyResDto> updatePenalty(
            @RequestParam
            Long penaltyId,
            @Valid
            @RequestBody
            PenaltyReqDto request
    ) {
        return BaseResponse.onSuccess(penaltyService.updatePenalty(penaltyId, request));
    }

    @DeleteMapping("/{penaltyId}")
    public BaseResponse<String> deletePenalty(
            @PathVariable
            Long penaltyId
    ) {
        penaltyService.deletePenalty(penaltyId);
        return BaseResponse.onSuccess("벌칙 삭제가 완료되었습니다.");
    }

    @PostMapping("/mission-complete")
    public BaseResponse<MissionCompleteResDto> missionComplete(
            @RequestHeader("Authorization")
            String authorizationHeader,
            @RequestParam
            Long roomId
    ) {
        return BaseResponse.onSuccess(penaltyService.missionComplete(authorizationHeader, roomId));
    }
}
