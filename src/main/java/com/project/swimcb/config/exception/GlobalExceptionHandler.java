package com.project.swimcb.config.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.NoSuchElementException;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
    val errorResponse = new ErrorResponse(NOT_FOUND.value(), e.getMessage());
    return ResponseEntity.status(NOT_FOUND).body(errorResponse);
  }
}
