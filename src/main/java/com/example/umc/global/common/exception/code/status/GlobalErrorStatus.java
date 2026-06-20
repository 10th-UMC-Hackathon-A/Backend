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
    _MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON407", "필수 요청 파라미터가 누락되었습니다."),
    _MISSING_HEADER(HttpStatus.BAD_REQUEST, "COMMON408", "필수 요청 헤더가 누락되었습니다."),
    _METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON410", "지원하지 않는 HTTP 메서드입니다."),
    _DATA_INTEGRITY_ERROR(HttpStatus.CONFLICT, "COMMON409", "이미 처리된 요청이거나 데이터 제약 조건에 위배됩니다."),
    _ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM404", "방을 찾을 수 없습니다."),
    _ROOM_DELETED(HttpStatus.GONE, "ROOM411", "종료되었거나 삭제된 방입니다."),
    _VOTE_CLOSED(HttpStatus.GONE, "ROOM410", "투표가 마감되었습니다. 페널티 조회로 이동하시오."),
    _ROUND_TRANSITIONED(HttpStatus.GONE, "ROOM412", "라운드가 전환되었습니다. 방 정보를 다시 조회해주세요."),
    _ALREADY_VOTED(HttpStatus.CONFLICT, "VOTE409", "이미 투표한 사용자입니다."),
    _VOTE_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "VOTE400", "존재하지 않는 투표 선택지입니다."),
    _DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "ROOM409", "이미 사용 중인 닉네임입니다."),
    _INVALID_NICKNAME_FORMAT(HttpStatus.BAD_REQUEST, "ROOM400", "허용되지 않는 닉네임 형식입니다."),
    _DRAW_CANDIDATE_NOT_FOUND(HttpStatus.CONFLICT, "DRAW409", "추첨 후보자가 없습니다."),
    _DRAW_RESULT_NOT_FOUND(HttpStatus.CONFLICT, "DRAW411", "아직 생성된 추첨 결과가 없습니다."),
    _DRAW_USER_RESULT_ALREADY_CREATED(HttpStatus.CONFLICT, "DRAW412", "이미 생성된 벌칙자 추첨 결과입니다."),
    _DRAW_PENALTY_RESULT_ALREADY_CREATED(HttpStatus.CONFLICT, "DRAW413", "이미 생성된 벌칙 추첨 결과입니다."),
    _PENALTY_NOT_FOUND(HttpStatus.NOT_FOUND, "PENALTY404", "벌칙을 찾을 수 없습니다."),
    _PENALTY_POOL_EMPTY(HttpStatus.CONFLICT, "PENALTY409", "등록된 벌칙이 없습니다."),
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
