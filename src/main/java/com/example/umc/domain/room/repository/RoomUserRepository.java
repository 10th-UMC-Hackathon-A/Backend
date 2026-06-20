package com.example.umc.domain.room.repository;

import com.example.umc.domain.room.entity.Room;
import com.example.umc.domain.room.entity.RoomUser;
import com.example.umc.domain.room.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {

    boolean existsByRoomAndUser(Room room, User user);

    boolean existsByRoomAndUser_NicknameAndUser_UidNot(Room room, String nickname, String uid);

    int countByRoom(Room room);
}
