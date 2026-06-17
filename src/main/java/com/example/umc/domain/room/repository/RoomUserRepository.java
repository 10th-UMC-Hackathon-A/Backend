package com.example.umc.domain.room.repository;

import com.example.umc.domain.room.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {
}
