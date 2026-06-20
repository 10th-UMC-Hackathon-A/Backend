package com.example.umc.global.security;

import com.example.umc.domain.room.dto.request.ParticipateRoomReqDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Duration accessExpiration;
    private final Duration refreshExpiration;

    public JwtUtil(
            @Value("${jwt.token.secretKey}") String secret,
            @Value("${jwt.token.expiration.access}") Long accessExpiration,
            @Value("${jwt.token.expiration.refresh}") Long refreshExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = Duration.ofMillis(accessExpiration);
        this.refreshExpiration = Duration.ofMillis(refreshExpiration);
    }

    public String createAccessToken(ParticipateRoomReqDto request) {
        return createAccessToken(request.uid(), request.nickName(), request.roomId());
    }

    public String createRefreshToken(ParticipateRoomReqDto request) {
        return createRefreshToken(request.uid(), request.nickName(), request.roomId());
    }

    public String createAccessToken(String uid, String nickName, Long roomId) {
        return createToken(uid, nickName, roomId, "access", accessExpiration);
    }

    public String createRefreshToken(String uid, String nickName, Long roomId) {
        return createToken(uid, nickName, roomId, "refresh", refreshExpiration);
    }

    /** 토큰에서 uid 가져오기
     *
     * @param token 유저 정보를 추출할 토큰
     * @return 유저 uid를 토큰에서 추출합니다
     */
    public String getUid(String token) {
        try {
            return getClaims(token).getPayload().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    public String getNickName(String token) {
        try {
            return getClaims(token).getPayload().get("nickName", String.class);
        } catch (JwtException e) {
            return null;
        }
    }

    public Long getRoomId(String token) {
        try {
            Object roomId = getClaims(token).getPayload().get("roomId");
            if (roomId instanceof Number number) {
                return number.longValue();
            }
            return Long.valueOf(String.valueOf(roomId));
        } catch (JwtException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    @Deprecated
    public String getEmail(String token) {
        return getUid(token);
    }

    /** 토큰 유효성 확인
     *
     * @param token 유효한지 확인할 토큰
     * @return True, False 반환합니다
     */
    public boolean isValid(String token) {
        return isValidToken(token, "access");
    }

    public boolean isValidRefreshToken(String token) {
        return isValidToken(token, "refresh");
    }

    public boolean isExpired(String token) {
        try {
            getClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isValidToken(String token, String tokenType) {
        try {
            Claims claims = getClaims(token).getPayload();
            return tokenType.equals(claims.get("tokenType", String.class));
        } catch (JwtException e) {
            return false;
        }
    }

    // 토큰 생성
    private String createToken(String uid, String nickName, Long roomId, String tokenType, Duration expiration) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(uid)
                .claim("uid", uid)
                .claim("nickName", nickName)
                .claim("roomId", roomId)
                .claim("tokenType", tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    // 토큰 정보 가져오기
    private Jws<Claims> getClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token);
    }
}
