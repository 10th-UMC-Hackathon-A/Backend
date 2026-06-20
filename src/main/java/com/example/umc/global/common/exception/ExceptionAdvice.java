package com.example.umc.global.common.exception;

import com.example.umc.global.common.base.BaseResponse;
import com.example.umc.global.common.exception.code.BaseCodeDto;
import com.example.umc.global.common.exception.code.status.GlobalErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    private static final String PENALTY_USER_DRAW_ROOM_ROUND_CONSTRAINT = "uk_penalty_user_draw_room_round";
    private static final String PENALTY_DRAW_ROOM_ROUND_CONSTRAINT = "uk_penalty_draw_room_round";

    /*
     * 직접 정의한 RestApiException 에러 클래스에 대한 예외 처리
     */
    // @ExceptionHandler는 Controller계층에서 발생하는 에러를 잡아서 메서드로 처리해주는 기능
    @ExceptionHandler(value = RestApiException.class)
    public ResponseEntity<BaseResponse<String>> handleRestApiException(RestApiException e) {
        BaseCodeDto errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    /*
     * 일반적인 서버 에러에 대한 예외 처리
     */
    @ExceptionHandler
    public ResponseEntity<BaseResponse<String>> handleException(Exception e) {
        e.printStackTrace(); //예외 정보 출력

        return handleExceptionInternalFalse(GlobalErrorStatus._INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    /*
     * ConstraintViolationException 발생 시 예외 처리
     * 메서드 파라미터, 또는 메서드 리턴 값에 문제가 있을 경우, @Validated 검증 실패한 경우
     */
    @ExceptionHandler
    public ResponseEntity<BaseResponse<String>> handleConstraintViolationException(ConstraintViolationException e) {
        return handleExceptionInternal(resolveDataIntegrityErrorCode(e));
    }

    /*
     * MethodArgumentTypeMismatchException 발생 시 예외 처리
     * 메서드의 인자 타입이 예상과 다른 경우
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<String>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException e) {
        // 예외 처리 로직
        return handleExceptionInternal(GlobalErrorStatus._METHOD_ARGUMENT_ERROR.getCode());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return handleExceptionInternal(resolveDataIntegrityErrorCode(e));
    }

    /*
     *  MethodArgumentNotValidException 발생 시 예외 처리
     * @@RequestBody 내부에서 처리 실패한 경우, @Valid 검증 실패한 경우 (ArgumnetResolver에 의해 유효성 검사)
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().stream()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage,
                            (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        log.warn("Validation Error: {}", errors);
        log.debug("Request URI: {}", request.getDescription(false));

        return handleExceptionInternalArgs(GlobalErrorStatus._VALIDATION_ERROR.getCode(), errors);

    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return handleExceptionInternalObject(GlobalErrorStatus._REQUEST_FORMAT_ERROR.getCode(), null);
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e, HttpHeaders headers, HttpStatusCode statusCode,
            WebRequest request) {
        return handleExceptionInternalObject(GlobalErrorStatus._MISSING_PARAMETER.getCode(), null);
    }

    @Override
    public ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException e, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (e instanceof MissingRequestHeaderException) {
            return handleExceptionInternalObject(GlobalErrorStatus._MISSING_HEADER.getCode(), null);
        }

        return handleExceptionInternalObject(GlobalErrorStatus._BAD_REQUEST.getCode(), null);
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException e, HttpHeaders headers, HttpStatusCode statusCode,
            WebRequest request) {
        return handleExceptionInternalObject(GlobalErrorStatus._METHOD_NOT_ALLOWED.getCode(), null);
    }

    private ResponseEntity<BaseResponse<String>> handleExceptionHttpMessage(BaseCodeDto errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

    private ResponseEntity<BaseResponse<String>> handleExceptionInternal(BaseCodeDto errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(BaseCodeDto errorCode, Map<String, String> errorArgs) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorArgs));
    }

    private ResponseEntity<Object> handleExceptionInternalObject(BaseCodeDto errorCode, Object errorArgs) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorArgs));
    }

    private BaseCodeDto resolveDataIntegrityErrorCode(Throwable e) {
        if (hasConstraint(e, PENALTY_USER_DRAW_ROOM_ROUND_CONSTRAINT)) {
            return GlobalErrorStatus._DRAW_USER_RESULT_ALREADY_CREATED.getCode();
        }

        if (hasConstraint(e, PENALTY_DRAW_ROOM_ROUND_CONSTRAINT)) {
            return GlobalErrorStatus._DRAW_PENALTY_RESULT_ALREADY_CREATED.getCode();
        }

        return GlobalErrorStatus._DATA_INTEGRITY_ERROR.getCode();
    }

    private boolean hasConstraint(Throwable e, String expectedConstraintName) {
        Throwable current = e;

        while (current != null) {
            if (current instanceof ConstraintViolationException constraintViolationException
                    && matchesConstraintName(constraintViolationException.getConstraintName(), expectedConstraintName)) {
                return true;
            }

            if (matchesConstraintName(current.getMessage(), expectedConstraintName)) {
                return true;
            }

            current = current.getCause();
        }

        return false;
    }

    private boolean matchesConstraintName(String actual, String expected) {
        return actual != null && actual.toLowerCase().contains(expected.toLowerCase());
    }

    private ResponseEntity<BaseResponse<String>> handleExceptionInternalFalse(BaseCodeDto errorCode,
                                                                              String errorPoint) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorPoint));
    }
}
