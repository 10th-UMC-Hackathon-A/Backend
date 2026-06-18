package com.example.umc.domain.penalty.repository;

import com.example.umc.domain.penalty.entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
}
