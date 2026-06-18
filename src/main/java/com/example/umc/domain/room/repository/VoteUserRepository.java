package com.example.umc.domain.room.repository;

import com.example.umc.domain.room.entity.VoteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteUserRepository extends JpaRepository<VoteUser, Long> {
}
