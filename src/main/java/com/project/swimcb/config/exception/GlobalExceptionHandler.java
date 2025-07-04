package com.project.swimcb.config.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Objects;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
    val errorResponse = new ErrorResponse(NOT_FOUND.value(), e.getMessage());
    return ResponseEntity.status(NOT_FOUND).body(errorResponse);
  }

  // @Valid 또는 @Validated가 @RequestBody(JSON) 객체에 붙었고, 그 내부 필드가 검증 조건을 위반했을 때
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    val errorResponse = new ErrorResponse(BAD_REQUEST.value(),
        Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
  }

  // 파라미터의 타입이 잘못 인입되었을 때 발생
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {

    val errorResponse = new ErrorResponse(BAD_REQUEST.value(),
        String.format("'%s' 파라미터의 타입이 '%s'가 아닙니다.", e.getName(), e.getRequiredType()));
    return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
  }

  // @Valid 또는 @Validated가 메서드 파라미터(Primitive, DTO 아닌) 에 붙었고, 그 파라미터에 직접 검증 제약이 있는 경우
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException e) {

    val errorResponse = new ErrorResponse(BAD_REQUEST.value(),
        Objects.requireNonNull(e.getMessage()));
    return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
  }

  // RequestBody가 JSON으로 아예 없거나, null이거나, 비어 있거나, JSON 파싱이 실패하면 바인딩 전에 예외를 던짐
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException e) {
    val errorResponse = new ErrorResponse(BAD_REQUEST.value(), e.getMessage());
    return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler({
      IllegalStateException.class,
      IllegalArgumentException.class
  })
  public ResponseEntity<ErrorResponse> handleBadRequestExceptions(RuntimeException e) {
    val errorResponse = new ErrorResponse(BAD_REQUEST.value(), e.getMessage());
    return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
    val errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage());
    return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
  }

}
