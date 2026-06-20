package com.example.umc.domain.room.repository;

import com.example.umc.domain.room.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteTypeRepository extends JpaRepository<VoteType, Long> {

    Optional<VoteType> findByLabel(String label);
}
