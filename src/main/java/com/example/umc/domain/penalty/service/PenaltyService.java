package com.example.umc.domain.penalty.service;

import com.example.umc.domain.penalty.dto.request.PenaltyReqDto;
import com.example.umc.domain.penalty.dto.response.MissionCompleteResDto;
import com.example.umc.domain.penalty.dto.response.PenaltyDrawResultResDto;
import com.example.umc.domain.penalty.dto.response.PenaltyResDto;
import com.example.umc.domain.penalty.dto.response.PenaltyUserDrawResultResDto;
import com.example.umc.domain.penalty.entity.Penalty;
import com.example.umc.domain.penalty.entity.PenaltyDrawResult;
import com.example.umc.domain.penalty.entity.PenaltyUserDrawResult;
import com.example.umc.domain.penalty.enums.GameTypeEnum;
import com.example.umc.domain.penalty.repository.PenaltyDrawResultRepository;
import com.example.umc.domain.penalty.repository.PenaltyRepository;
import com.example.umc.domain.penalty.repository.PenaltyUserDrawResultRepository;
import com.example.umc.domain.room.dto.request.VoteReqDto;
import com.example.umc.domain.room.dto.response.VoteStatusResDto;
import com.example.umc.domain.room.entity.Room;
import com.example.umc.domain.room.entity.User;
import com.example.umc.domain.room.entity.VoteType;
import com.example.umc.domain.room.entity.VoteUser;
import com.example.umc.domain.room.repository.RoomRepository;
import com.example.umc.domain.room.repository.UserRepository;
import com.example.umc.domain.room.repository.VoteUserRepository;
import com.example.umc.global.common.exception.RestApiException;
import com.example.umc.global.common.exception.code.status.AuthErrorStatus;
import com.example.umc.global.common.exception.code.status.GlobalErrorStatus;
import com.example.umc.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final PenaltyUserDrawResultRepository penaltyUserDrawResultRepository;
    private final PenaltyDrawResultRepository penaltyDrawResultRepository;
    private final RoomRepository roomRepository;
    private final VoteUserRepository voteUserRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PenaltyResDto createPenalty(PenaltyReqDto request) {
        Penalty penalty = Penalty.builder()
                .penaltyName(request.label())
                .build();

        Penalty savedPenalty = penaltyRepository.save(penalty);
        return toPenaltyResDto(savedPenalty);
    }

    @Transactional(readOnly = true)
    public List<PenaltyResDto> getPenalties() {
        return penaltyRepository.findAllByDeletedAtIsNull().stream()
                .map(this::toPenaltyResDto)
                .toList();
    }

    @Transactional
    public PenaltyUserDrawResultResDto drawPenaltyUser(Long roomId) {
        Room room = getRoom(roomId);

        return penaltyUserDrawResultRepository.findByRoomAndDrawRound(room, room.getDrawRound())
                .map(this::toPenaltyUserDrawResultResDto)
                .orElseGet(() -> createPenaltyUserDrawResult(room, room.getDrawRound()));
    }

    @Transactional
    public PenaltyDrawResultResDto drawPenalty(Long roomId) {
        Room room = getRoom(roomId);

        return penaltyDrawResultRepository.findByRoomAndDrawRound(room, room.getDrawRound())
                .map(this::toPenaltyDrawResultResDto)
                .orElseGet(() -> createPenaltyDrawResult(room, room.getDrawRound()));
    }

    @Transactional
    public PenaltyResDto updatePenalty(Long penaltyId, PenaltyReqDto request) {
        int updatedCount = penaltyRepository.updatePenaltyName(penaltyId, request.label());

        if (updatedCount == 0) {
            throw new RestApiException(GlobalErrorStatus._NOT_FOUND);
        }

        return toPenaltyResDto(getPenalty(penaltyId));
    }

    @Transactional
    public void deletePenalty(Long penaltyId) {
        Penalty penalty = getPenalty(penaltyId);
        penalty.delete();
    }

    @Transactional
    public MissionCompleteResDto missionComplete(String authorizationHeader, Long roomId) {
        User user = getUserFromAuthorizationHeader(authorizationHeader);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
        PenaltyUserDrawResult drawResult = penaltyUserDrawResultRepository.findByRoomAndDrawRound(room, room.getDrawRound())
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        String drawUserName = drawResult.getDrawUserList().get(drawResult.getWinnerIndex());

        if(!drawUserName.equals(user.getNickname())) {
           throw new RestApiException(GlobalErrorStatus._WRONG_DRAW_USER);
        }

        voteUserRepository.deleteByRoom(room);
        roomRepository.completeMission(roomId);

        return new MissionCompleteResDto(roomId);
    }

    private Penalty getPenalty(Long penaltyId) {
        return penaltyRepository.findByPenaltyIdAndDeletedAtIsNull(penaltyId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    private Room getRoom(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    private PenaltyUserDrawResultResDto createPenaltyUserDrawResult(Room room, Integer drawRound) {
        List<VoteUser> voteUsers = voteUserRepository.findByRoom(room);

        if (voteUsers.isEmpty()) {
            throw new RestApiException(GlobalErrorStatus._NOT_FOUND);
        }

        // 1. 랜덤 셔플
        Collections.shuffle(voteUsers, secureRandom);

        // 2. 당첨 인덱스 선정
        int winnerIndex = secureRandom.nextInt(voteUsers.size());
        GameTypeEnum penaltyType = getRandomPenaltyType();

        List<String> shuffledUserList = voteUsers.stream()
                .map(v -> v.getUser().getNickname())
                .toList();

        User selectedUser = voteUsers.get(winnerIndex).getUser();

        // 3. 결과 저장
        PenaltyUserDrawResult drawResult = penaltyUserDrawResultRepository.save(
                PenaltyUserDrawResult.builder()
                        .room(room)
                        .drawRound(drawRound)
                        .user(selectedUser)
                        .penaltyType(penaltyType)
                        .winnerIndex(winnerIndex)
                        .drawUserList(shuffledUserList)
                        .build()
        );

        return toPenaltyUserDrawResultResDto(drawResult);
    }

    private PenaltyDrawResultResDto createPenaltyDrawResult(Room room, Integer drawRound) {
        List<Penalty> penalties = penaltyRepository.findAllByDeletedAtIsNull();

        if (penalties.isEmpty()) {
            throw new RestApiException(GlobalErrorStatus._NOT_FOUND);
        }

        // 1. 랜덤 셔플
        Collections.shuffle(penalties, secureRandom);

        // 2. 당첨 인덱스 선정
        int prizeIndex = secureRandom.nextInt(penalties.size());
        GameTypeEnum penaltyType = getRandomPenaltyType();

        List<String> shuffledPenaltyList = penalties.stream()
                .map(Penalty::getPenaltyName)
                .collect(Collectors.toList());

        // 3. 당첨 결과 저장
        PenaltyDrawResult drawResult = penaltyDrawResultRepository.save(
                PenaltyDrawResult.builder()
                        .penalty(penalties.get(prizeIndex))
                        .penaltyType(penaltyType)
                        .room(room)
                        .drawRound(drawRound)
                        .prizeIndex(prizeIndex)
                        .drwaPenaltyList(shuffledPenaltyList)
                        .build()
        );

        return toPenaltyDrawResultResDto(drawResult);
    }

    private GameTypeEnum getRandomPenaltyType() {
        GameTypeEnum[] penaltyTypes = GameTypeEnum.values();
        return penaltyTypes[secureRandom.nextInt(penaltyTypes.length)];
    }

    private PenaltyResDto toPenaltyResDto(Penalty penalty) {
        return new PenaltyResDto(Math.toIntExact(penalty.getPenaltyId()), penalty.getPenaltyName());
    }

    private PenaltyUserDrawResultResDto toPenaltyUserDrawResultResDto(
            PenaltyUserDrawResult drawResult
    ) {
        User user = drawResult.getUser();

        return new PenaltyUserDrawResultResDto(
                drawResult.getRoom().getRoomId(),
                drawResult.getDrawRound(),
                user.getNickname(),
                drawResult.getPenaltyType(),
                drawResult.getWinnerIndex(),
                drawResult.getDrawUserList()
        );
    }

    private PenaltyDrawResultResDto toPenaltyDrawResultResDto(
            PenaltyDrawResult drawResult
    ) {
        return new PenaltyDrawResultResDto(
                drawResult.getRoom().getRoomId(),
                drawResult.getDrawRound(),
                drawResult.getPenalty().getPenaltyName(),
                drawResult.getPenaltyType(),
                drawResult.getPrizeIndex(),
                drawResult.getDrwaPenaltyList()
        );
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
}
