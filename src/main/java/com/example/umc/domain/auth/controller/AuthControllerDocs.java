package com.example.umc.domain.auth.controller;

import com.example.umc.domain.room.dto.request.TokenRefreshReqDto;
import com.example.umc.domain.room.dto.response.TokenRefreshResDto;
import com.example.umc.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "인증 관련 API")
public interface AuthControllerDocs {
    @Operation(summary = "JWT 토큰 재발급", description = "refreshToken을 검증한 뒤 새로운 accessToken과 refreshToken을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "JWT 토큰 재발급 성공")
    })
    BaseResponse<TokenRefreshResDto> refreshToken(
            @RequestBody(description = "JWT 토큰 재발급 요청 정보", required = true)
            TokenRefreshReqDto request
    );
}
