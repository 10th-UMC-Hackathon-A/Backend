package com.example.umc.domain.penalty.service;

import com.example.umc.domain.penalty.dto.request.PenaltyReqDto;
import com.example.umc.domain.penalty.dto.response.PenaltyResDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PenaltyService {

    public PenaltyResDto createPenalty(PenaltyReqDto request) {
        return new PenaltyResDto(1, request.label());
    }

    public List<PenaltyResDto> getPenalties() {
        return List.of(
                new PenaltyResDto(1, "춤추면서 에어컨 1도 낮추기"),
                new PenaltyResDto(2, "칠판에 오늘의 온도 적기")
        );
    }

    public PenaltyResDto updatePenalty(Long penaltyId, PenaltyReqDto request) {
        return new PenaltyResDto(penaltyId.intValue(), request.label());
    }

    public void deletePenalty(Long penaltyId) {
    }
}
