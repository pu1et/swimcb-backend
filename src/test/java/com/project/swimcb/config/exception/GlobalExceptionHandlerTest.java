package com.project.swimcb.config.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @InjectMocks
  private GlobalExceptionHandler handler;

  private static final String ERROR_MESSAGE = "에러 메시지";

  @Nested
  @DisplayName("NoSuchElementException 처리 시")
  class NoSuchElementExceptionTest {

    @Test
    @DisplayName("NOT_FOUND 상태 코드와 메시지를 담은 응답을 반환한다")
    void handleNoSuchElementException() {
      // given
      val exception = new NoSuchElementException(ERROR_MESSAGE);

      // when
      val response = handler.handleNoSuchElementException(exception);

      // then
      assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
      assertThat(response.getBody().status()).isEqualTo(NOT_FOUND.value());
      assertThat(response.getBody().message()).isEqualTo(ERROR_MESSAGE);
    }
  }

  @Nested
  @DisplayName("MethodArgumentNotValidException 처리 시")
  class MethodArgumentNotValidExceptionTest {

    @Test
    @DisplayName("BAD_REQUEST 상태 코드와 필드 에러 메시지를 담은 응답을 반환한다")
    void handleMethodArgumentNotValidException() {
      // given
      val exception = mock(MethodArgumentNotValidException.class);
      val bindingResult = mock(BindingResult.class);
      val fieldError = mock(FieldError.class);

      when(exception.getBindingResult()).thenReturn(bindingResult);
      when(bindingResult.getFieldError()).thenReturn(fieldError);
      when(fieldError.getDefaultMessage()).thenReturn(ERROR_MESSAGE);

      // when
      ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentNotValidException(
          exception);

      // then
      assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
      assertThat(response.getBody().status()).isEqualTo(BAD_REQUEST.value());
      assertThat(response.getBody().message()).isEqualTo(ERROR_MESSAGE);
    }
  }

  @Nested
  @DisplayName("ConstraintViolationException 처리 시")
  class ConstraintViolationExceptionTest {

    @Test
    @DisplayName("BAD_REQUEST 상태 코드와 제약 위반 메시지를 담은 응답을 반환한다")
    void handleConstraintViolationException() {
      // given
      val exception = mock(ConstraintViolationException.class);
      when(exception.getMessage()).thenReturn(ERROR_MESSAGE);

      // when
      val response = handler.handleConstraintViolationException(exception);

      // then
      assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
      assertThat(response.getBody().status()).isEqualTo(BAD_REQUEST.value());
      assertThat(response.getBody().message()).isEqualTo(ERROR_MESSAGE);
    }
  }

  @Nested
  @DisplayName("IllegalArgumentException과 IllegalStateException 처리 시")
  class BadRequestExceptionsTest {

    @Test
    @DisplayName("IllegalArgumentException은 BAD_REQUEST 상태 코드와 메시지를 담은 응답을 반환한다")
    void handleIllegalArgumentException() {
      // given
      val exception = new IllegalArgumentException(ERROR_MESSAGE);

      // when
      val response = handler.handleBadRequestExceptions(exception);

      // then
      assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
      assertThat(response.getBody().status()).isEqualTo(BAD_REQUEST.value());
      assertThat(response.getBody().message()).isEqualTo(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("IllegalStateException은 BAD_REQUEST 상태 코드와 메시지를 담은 응답을 반환한다")
    void handleIllegalStateException() {
      // given
      val exception = new IllegalStateException(ERROR_MESSAGE);

      // when
      val response = handler.handleBadRequestExceptions(exception);

      // then
      assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
      assertThat(response.getBody().status()).isEqualTo(BAD_REQUEST.value());
      assertThat(response.getBody().message()).isEqualTo(ERROR_MESSAGE);
    }
  }

  @Nested
  @DisplayName("HttpMessageNotReadableException 처리 시")
  class HttpMessageNotReadableExceptionTest {

    @Test
    @DisplayName("BAD_REQUEST 상태 코드와 제약 위반 메시지를 담은 응답을 반환한다")
    void handleHttpMessageNotReadableException() {
      // given
      val exception = mock(HttpMessageNotReadableException.class);
      when(exception.getMessage()).thenReturn(ERROR_MESSAGE);

      // when
      val response = handler.handleHttpMessageNotReadable(exception);

      // then
      assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
      assertThat(response.getBody().status()).isEqualTo(BAD_REQUEST.value());
      assertThat(response.getBody().message()).isEqualTo(ERROR_MESSAGE);
    }
  }

  @Nested
  @DisplayName("기타 RuntimeException 처리 시")
  class RuntimeExceptionTest {

    @Test
    @DisplayName("INTERNAL_SERVER_ERROR 상태 코드와 메시지를 담은 응답을 반환한다")
    void handleRuntimeException() {
      // given
      val exception = new RuntimeException(ERROR_MESSAGE);

      // when
      val response = handler.handleRuntimeException(exception);

      // then
      assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
      assertThat(response.getBody().status()).isEqualTo(INTERNAL_SERVER_ERROR.value());
      assertThat(response.getBody().message()).isEqualTo(ERROR_MESSAGE);
    }
  }
}