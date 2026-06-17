package com.example.umc.domain.room.repository;

import com.example.umc.domain.room.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteTypeRepository extends JpaRepository<VoteType, Long> {
}
