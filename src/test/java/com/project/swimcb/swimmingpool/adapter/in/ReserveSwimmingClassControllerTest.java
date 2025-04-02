package com.project.swimcb.swimmingpool.adapter.in;

import static com.project.swimcb.swimmingpool.adapter.in.ReserveSwimmingClassControllerTest.MEMBER_ID;
import static com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus.RESERVABLE;
import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.BANK_TRANSFER;
import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.swimmingpool.application.in.ReserveSwimmingClassUseCase;
import com.project.swimcb.swimmingpool.domain.ReservationInfo;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = ReserveSwimmingClassController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class ReserveSwimmingClassControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ReserveSwimmingClassUseCase useCase;

  static final String MEMBER_ID = "1";
  private static final String PATH = "/api/swimming-classes/{swimmingClassId}/reservations";

  @Nested
  @DisplayName("수영 클래스 예약 API")
  class ReserveSwimmingClass {

    private final long SWIMMING_CLASS_ID = 2L;
    private final long TICKET_ID = 3L;
    private ReservationInfo reservationInfo;

    @BeforeEach
    void setUp() {
      reservationInfo = new ReservationInfo(1L, RESERVABLE, 1);
    }

    @Test
    @DisplayName("정상적인 요청이 들어오면 예약이 성공적으로 처리된다.")
    void shouldReserveSwimmingClass() throws Exception {
      // given
      val request = new ReserveSwimmingClassRequest(TICKET_ID, BANK_TRANSFER);
      val expectedResponse = new ReserveSwimmingClassResponse(reservationInfo.id(),
          reservationInfo.availabilityStatus(), reservationInfo.waitingNo());

      when(useCase.reserveSwimmingClass(any())).thenReturn(reservationInfo);
      // when
      // then
      mockMvc.perform(post(PATH, SWIMMING_CLASS_ID)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

      verify(useCase, only()).reserveSwimmingClass(assertArg(i -> {
        assertThat(i.memberId()).isEqualTo(Long.parseLong(MEMBER_ID));
        assertThat(i.swimmingClassId()).isEqualTo(SWIMMING_CLASS_ID);
        assertThat(i.ticketId()).isEqualTo(TICKET_ID);
        assertThat(i.paymentMethod()).isEqualTo(BANK_TRANSFER);
      }));
    }

    @Test
    @DisplayName("결제 방식이 null이면 400 에러가 반환된다.")
    void shouldReturn400WhenPaymentMethodIsNull() throws Exception {
      // given
      val request = new ReserveSwimmingClassRequest(3L, null);
      // when
      // then
      mockMvc.perform(post(PATH, SWIMMING_CLASS_ID)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(content().string(containsString("결제 수단은 null이 될 수 없습니다.")));
    }

    @Test
    @DisplayName("다양한 결제 방식으로 예약이 성공적으로 처리된다.")
    void shouldReserveSwimmingClassWithDifferentPaymentMethod() throws Exception {
      // given
      val request = new ReserveSwimmingClassRequest(TICKET_ID, CASH_ON_SITE);

      when(useCase.reserveSwimmingClass(any())).thenReturn(reservationInfo);
      // when
      // then
      mockMvc.perform(post(PATH, SWIMMING_CLASS_ID)
              .contentType(APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk());

      verify(useCase, only()).reserveSwimmingClass(assertArg(i -> {
        assertThat(i.paymentMethod()).isEqualTo(CASH_ON_SITE);
      }));
    }

    @Test
    @DisplayName("잘못된 결제 수단이 들어오면 400 에러가 반환된다.")
    void shouldReturn400WhenInvalidPaymentMethod() throws Exception {
      // given
      val invalidPaymentJson = "{\"ticketId\": 3, \"paymentMethod\": \"INVALID_METHOD\"}";
      // when
      // then
      mockMvc.perform(post(PATH, SWIMMING_CLASS_ID)
              .contentType(APPLICATION_JSON)
              .content(invalidPaymentJson))
          .andExpect(status().isBadRequest());
    }
  }
}