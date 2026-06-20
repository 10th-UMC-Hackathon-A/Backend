package com.example.umc.domain.room.repository;

import com.example.umc.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;


public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomId(Long roomId);

    Room findByRoomIdAndDeletedAtIsNull(Long roomId);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Room r
            SET r.roomName = :roomName
            WHERE r.roomId = :roomId
                and r.deletedAt is null
            """)
    int updateRoomName(String roomName, Long roomId);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE Room r
            SET r.voteStartedAt = :voteStartedAt,
                r.voteClosedAt = :voteClosedAt
            WHERE r.roomId = :roomId
                and r.voteStartedAt is null
                and r.deletedAt is null
            """)
    int updateVoteTime(Long roomId, LocalDateTime voteStartedAt, LocalDateTime voteClosedAt);

    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE Room r
        SET r.drawRound = r.drawRound + 1,
            r.voteStartedAt = null,
            r.voteClosedAt = null
        WHERE r.roomId = :roomId
            and r.deletedAt is null
        """)
    int completeMission(Long roomId);
}
