package com.example.umc.domain.auth.controller;

import com.example.umc.domain.auth.service.AuthService;
import com.example.umc.domain.room.dto.request.TokenRefreshReqDto;
import com.example.umc.domain.room.dto.response.TokenRefreshResDto;
import com.example.umc.global.common.base.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {
    private final AuthService authService;

    @PostMapping("/token/refresh")
    public BaseResponse<TokenRefreshResDto> refreshToken(
            @RequestBody
            TokenRefreshReqDto request
    ) {
        return BaseResponse.onSuccess(authService.refreshToken(request));
    }
}
