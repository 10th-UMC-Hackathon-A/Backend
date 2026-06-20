package com.example.umc.domain.room.repository;

import com.example.umc.domain.room.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUid(String uid);
}
