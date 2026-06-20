package com.example.umc.domain.penalty.repository;

import com.example.umc.domain.penalty.entity.PenaltyDrawResult;
import com.example.umc.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PenaltyDrawResultRepository extends JpaRepository<PenaltyDrawResult, Long> {

    Optional<PenaltyDrawResult> findByRoomAndDrawRound(Room room, Integer drawRound);
}
