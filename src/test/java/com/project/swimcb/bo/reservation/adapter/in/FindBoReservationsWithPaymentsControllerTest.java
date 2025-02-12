package com.project.swimcb.bo.reservation.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.config.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FindBoReservationsWithPaymentsController.class)
@Import({SecurityConfig.class})
class FindBoReservationsWithPaymentsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private static final String PATH = "/api/bo/reservations-with-payments";

  @Test
  @DisplayName("정상적인 요청이 들어온 경우 조회 성공")
  void shouldReturnReservationsWithPayments() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get(PATH)
            .param("start-date", "2025-01-01")
            .param("end-date", "2025-12-31")
            .param("swimming-type", "SWIMMING_CLASS")
            .param("reservation-payment-status", "RESERVATION_REQUESTED")
            .param("page", "1")
            .param("size", "10"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("page가 1 미만일 경우 400 반환")
  void shouldReturn400WhenPageIsLessThan1() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get(PATH)
            .param("start-date", "2025-01-01")
            .param("end-date", "2025-12-31")
            .param("swimming-type", "SWIMMING_CLASS")
            .param("reservation-payment-status", "RESERVATION_REQUESTED")
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("page는 1 이상이어야 합니다.")));
  }

  @Test
  @DisplayName("size가 1 미만일 경우 400 반환")
  void shouldReturn400WhenSizeIsLessThan1() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get(PATH)
            .param("start-date", "2025-01-01")
            .param("end-date", "2025-12-31")
            .param("swimming-type", "SWIMMING_CLASS")
            .param("reservation-payment-status", "RESERVATION_REQUESTED")
            .param("page", "1")
            .param("size", "0"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("size는 1 이상이어야 합니다.")));
  }
}
