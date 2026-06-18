package com.example.umc.domain.panalty.repository;

import com.example.umc.domain.panalty.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
}
