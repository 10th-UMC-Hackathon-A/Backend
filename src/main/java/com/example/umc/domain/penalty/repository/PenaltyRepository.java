package com.example.umc.domain.penalty.repository;

import com.example.umc.domain.penalty.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    List<Penalty> findAllByDeletedAtIsNull();

    Optional<Penalty> findByPenaltyIdAndDeletedAtIsNull(Long penaltyId);
}
