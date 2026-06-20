package com.example.umc.domain.auth.service;

import com.example.umc.domain.room.dto.request.ParticipateRoomReqDto;
import com.example.umc.domain.room.dto.request.TokenRefreshReqDto;
import com.example.umc.domain.room.dto.response.TokenRefreshResDto;
import com.example.umc.domain.room.entity.Room;
import com.example.umc.domain.room.entity.User;
import com.example.umc.domain.room.repository.RoomRepository;
import com.example.umc.domain.room.repository.RoomUserRepository;
import com.example.umc.domain.room.repository.UserRepository;
import com.example.umc.global.common.exception.RestApiException;
import com.example.umc.global.common.exception.code.status.AuthErrorStatus;
import com.example.umc.global.common.exception.code.status.GlobalErrorStatus;
import com.example.umc.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;

    public String createAccessToken(ParticipateRoomReqDto request) {
        return jwtUtil.createAccessToken(request);
    }

    public String createRefreshToken(ParticipateRoomReqDto request) {
        return jwtUtil.createRefreshToken(request);
    }

    public TokenRefreshResDto refreshToken(TokenRefreshReqDto request) {
        if (request == null || request.refreshToken() == null || request.refreshToken().isBlank()) {
            throw new RestApiException(AuthErrorStatus.EMPTY_JWT);
        }

        String refreshToken = request.refreshToken();
        if (!jwtUtil.isValidRefreshToken(refreshToken)) {
            if (jwtUtil.isExpired(refreshToken)) {
                throw new RestApiException(AuthErrorStatus.EXPIRED_REFRESH_TOKEN);
            }

            throw new RestApiException(AuthErrorStatus.INVALID_REFRESH_TOKEN);
        }

        String uid = jwtUtil.getUid(refreshToken);
        String nickName = jwtUtil.getNickName(refreshToken);
        Long roomId = jwtUtil.getRoomId(refreshToken);

        if (uid == null || nickName == null || roomId == null) {
            throw new RestApiException(AuthErrorStatus.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new RestApiException(AuthErrorStatus.USER_NOT_FOUND));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        if (!roomUserRepository.existsByRoomAndUser(room, user)) {
            throw new RestApiException(AuthErrorStatus.INVALID_ROLE);
        }

        return new TokenRefreshResDto(
                jwtUtil.createAccessToken(uid, nickName, roomId),
                jwtUtil.createRefreshToken(uid, nickName, roomId)
        );
    }

    public User getUserFromAuthorizationHeader(String authorizationHeader) {
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
