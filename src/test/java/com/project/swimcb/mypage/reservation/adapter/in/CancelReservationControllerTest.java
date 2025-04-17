package com.project.swimcb.mypage.reservation.adapter.in;

import static com.project.swimcb.mypage.reservation.adapter.in.CancelReservationControllerTest.MEMBER_ID;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.mypage.reservation.application.port.in.CancelReservationUseCase;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CancelReservationController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class CancelReservationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CancelReservationUseCase useCase;

  static final String MEMBER_ID = "1";

  private static final String PATH = "/api/my-page/reservations/{reservationId}/cancel";

  @Test
  @DisplayName("인증된 사용자의 유효한 예약 ID로 요청 시 예약을 취소한다")
  void cancelReservationWhenValidIdProvided() throws Exception {
    // Given
    val memberId = 1L;
    val reservationId = 2L;

    // when
    // then
    mockMvc.perform(put(PATH, reservationId))
        .andExpect(status().isOk());

    verify(useCase, only()).cancelReservation(memberId, reservationId);
  }

  @Test
  @DisplayName("예약 취소 중 예외가 발생하면 컨트롤러는 해당 예외를 그대로 전파한다")
  void shouldPropagateExceptionWhenUseCaseThrowsException() throws Exception {
    // given
    val memberId = 1L;
    val reservationId = 999L;

    doThrow(new NoSuchElementException("예약을 찾을 수 없습니다 : " + reservationId))
        .when(useCase).cancelReservation(anyLong(), anyLong());

    // when
    // then
    mockMvc.perform(put(PATH, reservationId))
        .andExpect(status().isNotFound()); // GlobalExceptionHandler가 404로 처리함

    verify(useCase, only()).cancelReservation(memberId, reservationId);
  }
}