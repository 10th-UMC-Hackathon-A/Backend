package com.example.umc.domain.room.repository;

import com.example.umc.domain.room.entity.Room;
import com.example.umc.domain.room.entity.User;
import com.example.umc.domain.room.entity.VoteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteUserRepository extends JpaRepository<VoteUser, Long> {

    List<VoteUser> findByRoom(Room room);

    void deleteByRoomAndUser(Room room, User user);
}
