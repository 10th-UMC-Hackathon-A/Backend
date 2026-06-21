package com.example.umc.domain.room.repository;

import com.example.umc.domain.room.entity.Room;
import com.example.umc.domain.room.entity.User;
import com.example.umc.domain.room.entity.VoteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteUserRepository extends JpaRepository<VoteUser, Long> {

    List<VoteUser> findByRoom(Room room);

    boolean existsByRoomAndUser(Room room, User user);

    void deleteByRoomAndUser(Room room, User user);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM vote_user WHERE room_id = :roomId", nativeQuery = true)
    int deleteAllByRoomId(@Param("roomId") Long roomId);
}
