package com.example.umc.domain.room.service;

import com.example.umc.domain.room.dto.request.ParticipateRoomReqDto;
import com.example.umc.domain.room.dto.request.RoomReqDto;
import com.example.umc.domain.room.dto.request.VoteTypeReqDto;
import com.example.umc.domain.room.dto.request.VoteReqDto;
import com.example.umc.domain.room.dto.response.ParticipantResDto;
import com.example.umc.domain.room.dto.response.RoomResDto;
import com.example.umc.domain.room.dto.response.VoteStatusResDto;
import com.example.umc.domain.room.dto.response.VoteStatusWithAliasResDto;
import com.example.umc.domain.room.dto.response.VoteTypeResDto;
import com.example.umc.domain.room.entity.Room;
import com.example.umc.domain.room.repository.RoomRepository;
import com.example.umc.global.common.exception.RestApiException;
import com.example.umc.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import com.example.umc.domain.room.entity.RoomUser;
import com.example.umc.domain.room.entity.User;
import com.example.umc.domain.room.entity.VoteType;
import com.example.umc.domain.room.entity.VoteUser;
import com.example.umc.domain.room.repository.RoomUserRepository;
import com.example.umc.domain.room.repository.UserRepository;
import com.example.umc.domain.room.repository.VoteTypeRepository;
import com.example.umc.domain.room.repository.VoteUserRepository;
import com.example.umc.global.common.exception.code.status.AuthErrorStatus;
import com.example.umc.global.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomUserRepository roomUserRepository;
    private final VoteTypeRepository voteTypeRepository;
    private final VoteUserRepository voteUserRepository;
    private final JwtUtil jwtUtil;

    @Value("${vote.deadline-seconds:30}")
    private long voteDeadlineSeconds;

    @Transactional
    public RoomResDto createRoom(RoomReqDto request) {
        Room room = toRoom(request);
        Room savedRoom = roomRepository.save(room);
        return toRoomResDto(savedRoom);
    }

    public List<RoomResDto> getRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(this::toRoomResDto)
                .toList();
    }

    @Transactional
    public VoteTypeResDto createVoteType(VoteTypeReqDto request) {
        VoteType voteType = VoteType.builder()
                .label(request.label())
                .build();
        VoteType savedVoteType = voteTypeRepository.save(voteType);
        return toVoteTypeResDto(savedVoteType);
    }

    public List<VoteTypeResDto> getVoteTypes() {
        return voteTypeRepository.findAll().stream()
                .map(this::toVoteTypeResDto)
                .toList();
    }

    public RoomResDto updateRoom(Long roomId, RoomReqDto request) {
        Room room = getMyActiveRoom(roomId);
        int updatedCount = roomRepository.updateRoomName(request.roomName(), room.getRoomId());

        if (updatedCount == 0) {
            throw new RestApiException(GlobalErrorStatus._VALIDATION_ERROR);
        }

        return toRoomResDto(getMyActiveRoom(roomId));
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = getMyActiveRoom(roomId);
        room.delete();
    }

    @Transactional
    public ParticipantResDto participateRoom(ParticipateRoomReqDto request) {
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();

        startVoteIfNeeded(room, now);
        validateVoteOpen(room, now);

        User user = userRepository.findByUid(request.uid())
                .orElseGet(() -> userRepository.save(User.builder()
                        .nickname(request.nickName())
                        .uid(request.uid())
                        .build()));

        if (!roomUserRepository.existsByRoomAndUser(room, user)) {
            roomUserRepository.save(RoomUser.builder()
                    .room(room)
                    .user(user)
                    .build());
        }

        return new ParticipantResDto(
                user.getUserId(),
                room.getRoomId(),
                user.getNickname(),
                user.getUid(),
                jwtUtil.createAccessToken(request)
        );
    }

    @Transactional
    public List<VoteStatusResDto> vote(String authorizationHeader, VoteReqDto request) {
        User user = getUserFromAuthorizationHeader(authorizationHeader);
        Room room = getMyActiveRoom(request.roomId());
        VoteType voteType = voteTypeRepository.findByLabel(request.position())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        if (!roomUserRepository.existsByRoomAndUser(room, user)) {
            throw new RestApiException(AuthErrorStatus.INVALID_ROLE);
        }

        voteUserRepository.deleteByRoomAndUser(room, user);
        voteUserRepository.save(VoteUser.builder()
                .room(room)
                .user(user)
                .voteType(voteType)
                .build());

        return getVoteStatus(room.getRoomId());
    }

    @Transactional(readOnly = true)
    public List<VoteStatusResDto> getVoteStatus(Long roomId) {
        Room room = getMyActiveRoom(roomId);
        Map<String, Long> voteCountByLabel = voteUserRepository.findByRoom(room).stream()
                .collect(Collectors.groupingBy(
                        voteUser -> voteUser.getVoteType().getLabel(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        return voteTypeRepository.findAll().stream()
                .map(voteType -> new VoteStatusResDto(
                        voteType.getLabel(),
                        voteCountByLabel.getOrDefault(voteType.getLabel(), 0L).intValue()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VoteStatusWithAliasResDto> getVoteStatusWithMembers(Long roomId) {
        Room room = getMyActiveRoom(roomId);
        Map<String, List<String>> nicknamesByLabel = voteUserRepository.findByRoom(room).stream()
                .collect(Collectors.groupingBy(
                        voteUser -> voteUser.getVoteType().getLabel(),
                        LinkedHashMap::new,
                        Collectors.mapping(
                                voteUser -> voteUser.getUser().getNickname(),
                                Collectors.toList()
                        )
                ));

        return voteTypeRepository.findAll().stream()
                .map(voteType -> new VoteStatusWithAliasResDto(
                        voteType.getLabel(),
                        nicknamesByLabel.getOrDefault(voteType.getLabel(), List.of())
                ))
                .toList();
    }

    private User getUserFromAuthorizationHeader(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        if (!jwtUtil.isValid(token)) {
            throw new RestApiException(AuthErrorStatus.INVALID_ACCESS_TOKEN);
        }

        return userRepository.findByUid(jwtUtil.getUid(token))
                .orElseThrow(() -> new RestApiException(AuthErrorStatus.USER_NOT_FOUND));
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RestApiException(AuthErrorStatus.EMPTY_JWT);
        }

        return authorizationHeader.substring(7);
    }

    private RoomResDto toRoomResDto(Room room) {
        return new RoomResDto(room.getRoomName(), room.getRoomId());
    }

    private VoteTypeResDto toVoteTypeResDto(VoteType voteType) {
        return new VoteTypeResDto(voteType.getVoteTypeId(), voteType.getLabel());
    }

    private void startVoteIfNeeded(Room room, LocalDateTime now) {
        if (room.getVoteStartedAt() == null) {
            roomRepository.updateVoteTime(
                    room.getRoomId(),
                    now,
                    now.plusSeconds(voteDeadlineSeconds)
            );
        }
    }

    private void validateVoteOpen(Room room, LocalDateTime now) {

        if (room.getVoteClosedAt() != null && now.isAfter(room.getVoteClosedAt())) {

            // 마감 후 5분이 지났는지 확인
            if (now.isAfter(room.getVoteClosedAt().plusMinutes(5))) {
                roomRepository.completeMission(room.getRoomId());
                return;
            }

            throw new RestApiException(GlobalErrorStatus._VOTE_CLOSED);
        }
    }

    private Room toRoom(RoomReqDto request) {
        return Room.builder()
                .roomName(request.roomName())
                .build();
    }


    private Room getMyActiveRoom(Long roomId) {
        if(!roomRepository.existsByRoomId(roomId)) {
            throw new RestApiException(GlobalErrorStatus._NOT_FOUND);
        }

        return roomRepository.findByRoomIdAndDeletedAtIsNull(roomId);
    }
}
