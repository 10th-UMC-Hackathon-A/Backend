package com.example.umc.domain.room.repository;

import com.example.umc.domain.room.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
