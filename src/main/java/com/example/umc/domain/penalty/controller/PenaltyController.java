package com.example.umc.domain.penalty.controller;

import com.example.umc.domain.penalty.dto.request.PenaltyReqDto;
import com.example.umc.domain.penalty.dto.response.PenaltyResDto;
import com.example.umc.domain.penalty.service.PenaltyService;
import com.example.umc.global.common.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/penalties")
public class PenaltyController {

    private final PenaltyService penaltyService;

    @PostMapping
    public BaseResponse<PenaltyResDto> createPenalty(@RequestBody PenaltyReqDto request) {
        return BaseResponse.onSuccess(penaltyService.createPenalty(request));
    }

    @GetMapping
    public BaseResponse<List<PenaltyResDto>> getPenaltyList() {
        return BaseResponse.onSuccess(penaltyService.getPenalties());
    }

    @PatchMapping("/update")
    public BaseResponse<PenaltyResDto> updatePenalty(
            @RequestParam Long penaltyId,
            @RequestBody PenaltyReqDto request
    ) {
        return BaseResponse.onSuccess(penaltyService.updatePenalty(penaltyId, request));
    }

    @DeleteMapping("/{penaltyId}")
    public BaseResponse<String> deletePenalty(@PathVariable Long penaltyId) {
        penaltyService.deletePenalty(penaltyId);
        return BaseResponse.onSuccess("벌칙 삭제가 완료되었습니다.");
    }
}
