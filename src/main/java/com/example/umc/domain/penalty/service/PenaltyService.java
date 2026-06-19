package com.example.umc.domain.penalty.service;

import com.example.umc.domain.penalty.dto.request.PenaltyReqDto;
import com.example.umc.domain.penalty.dto.response.PenaltyResDto;
import com.example.umc.domain.penalty.entity.Penalty;
import com.example.umc.domain.penalty.repository.PenaltyRepository;
import com.example.umc.global.common.exception.RestApiException;
import com.example.umc.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;

    @Transactional
    public PenaltyResDto createPenalty(PenaltyReqDto request) {
        Penalty penalty = Penalty.builder()
                .penaltyName(request.label())
                .build();

        Penalty savedPenalty = penaltyRepository.save(penalty);
        return toPenaltyResDto(savedPenalty);
    }

    @Transactional(readOnly = true)
    public List<PenaltyResDto> getPenalties() {
        return penaltyRepository.findAllByDeletedAtIsNull().stream()
                .map(this::toPenaltyResDto)
                .toList();
    }

    @Transactional
    public PenaltyResDto updatePenalty(Long penaltyId, PenaltyReqDto request) {
        int updatedCount = penaltyRepository.updatePenaltyName(penaltyId, request.label());

        if (updatedCount == 0) {
            throw new RestApiException(GlobalErrorStatus._NOT_FOUND);
        }

        return toPenaltyResDto(getPenalty(penaltyId));
    }

    @Transactional
    public void deletePenalty(Long penaltyId) {
        Penalty penalty = getPenalty(penaltyId);
        penalty.delete();
    }

    private Penalty getPenalty(Long penaltyId) {
        return penaltyRepository.findByPenaltyIdAndDeletedAtIsNull(penaltyId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    private PenaltyResDto toPenaltyResDto(Penalty penalty) {
        return new PenaltyResDto(Math.toIntExact(penalty.getPenaltyId()), penalty.getPenaltyName());
    }
}
