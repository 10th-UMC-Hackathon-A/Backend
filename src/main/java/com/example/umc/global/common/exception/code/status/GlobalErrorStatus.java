package com.example.umc.global.common.exception.code.status;

import com.example.umc.global.common.exception.code.BaseCodeDto;
import com.example.umc.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorStatus implements BaseCodeInterface {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON402", "Validation Error입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 정보를 찾을 수 없습니다."),
    _METHOD_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "COMMON405", "Argument Type이 올바르지 않습니다."),
    _REQUEST_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "COMMON406", "요청 본문 형식이 올바르지 않습니다. Enum 값이나 다른 데이터 형식을 확인해주세요."),
    _VOTE_CLOSED(HttpStatus.GONE, "ROOM410", "투표가 마감되었습니다. 페널티 조회로 이동하시오."),
    _ALREADY_VOTED(HttpStatus.CONFLICT, "VOTE409", "이미 투표한 사용자입니다."),
    _DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "ROOM409", "이미 사용 중인 닉네임입니다."),
    _INVALID_NICKNAME_FORMAT(HttpStatus.BAD_REQUEST, "ROOM400", "허용되지 않는 닉네임 형식입니다."),
    _WRONG_DRAW_USER(HttpStatus.BAD_REQUEST, "DRAW400", "벌칙 대상자가 아닙니다."),
    _INTERNAL_PAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "페이지 에러, 0 이상의 페이지를 입력해주세요");

    private final HttpStatus httpStatus;
    private final boolean isSuccess = false;
    private final String code;
    private final String message;

    @Override
    public BaseCodeDto getCode() {
        return BaseCodeDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }
}
