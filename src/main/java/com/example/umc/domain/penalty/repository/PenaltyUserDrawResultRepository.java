package com.example.umc.domain.penalty.repository;

import com.example.umc.domain.penalty.entity.PenaltyUserDrawResult;
import com.example.umc.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PenaltyUserDrawResultRepository extends JpaRepository<PenaltyUserDrawResult, Long> {

    Optional<PenaltyUserDrawResult> findByRoomAndDrawRound(Room room, Integer drawRound);
}
