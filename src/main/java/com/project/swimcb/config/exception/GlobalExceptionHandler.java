package com.project.swimcb.config.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Objects;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
    val errorResponse = new ErrorResponse(NOT_FOUND.value(), e.getMessage());
    return ResponseEntity.status(NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {

    val errorResponse = new ErrorResponse(BAD_REQUEST.value(),
        Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException e) {

    val errorResponse = new ErrorResponse(BAD_REQUEST.value(),
        Objects.requireNonNull(e.getMessage()));
    return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    val errorResponse = new ErrorResponse(BAD_REQUEST.value(), e.getMessage());
    return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
  }
}
