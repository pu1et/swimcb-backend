package com.project.swimcb.reservation.adapter.in;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.reservation.application.port.in.FindReservationUseCase;
import com.project.swimcb.reservation.domain.ReservationInfo;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindReservationInfoController.class)
class FindReservationInfoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindReservationUseCase useCase;

  @MockitoBean
  private FindReservationResponseMapper mapper;

  private static final String PATH = "/api/swimming-classes/reservations/{reservationId}";

  @Nested
  @DisplayName("예약 정보 조회 API는")
  class FindReservation {

    @Test
    @DisplayName("유효한 예약 ID로 요청 시 예약 정보를 응답한다")
    void returnReservationInfowhenValidIdProvided() throws Exception {
      // given
      val reservationId = 1L;

      val reservationInfo = TestReservationInfoFactory.create();
      val response = TestFindReservationResponseFactory.create();

      when(useCase.findReservation(reservationId)).thenReturn(reservationInfo);
      when(mapper.toResponse(reservationInfo)).thenReturn(response);

      // when
      // then
      mockMvc.perform(get(PATH, reservationId))
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(response)));

      verify(useCase, only()).findReservation(reservationId);
      verify(mapper, only()).toResponse(reservationInfo);
    }
  }

  private static class TestReservationInfoFactory {

    private static ReservationInfo create() {
      return ReservationInfo.builder()
          .swimmingPool(
              ReservationInfo.SwimmingPool.builder()
                  .id(1L)
                  .name("DUMMY_POOL_NAME")
                  .accountNo(AccountNo.of("DUMMY_ACCOUNT_NO"))
                  .build()
          )
          .swimmingClass(
              ReservationInfo.SwimmingClass.builder()
                  .id(2L)
                  .month(5)
                  .type(GROUP)
                  .subType("DUMMY_CLASS_SUB_TYPE")
                  .daysOfWeek(0b1010100) // 월,수,금
                  .startTime(LocalTime.of(10, 0))
                  .endTime(LocalTime.of(11, 0))
                  .build()
          )
          .ticket(
              ReservationInfo.Ticket.builder()
                  .id(3L)
                  .name("DUMMY_TICKET_NAME")
                  .price(50000)
                  .build()
          )
          .reservation(
              ReservationInfo.Reservation.builder()
                  .id(4L)
                  .reservedAt(LocalDateTime.of(2025, 4, 1, 10, 0))
                  .build()
          )
          .payment(
              ReservationInfo.Payment.builder()
                  .method(CASH_ON_SITE)
                  .build()
          )
          .build();
    }
  }

  private static class TestFindReservationResponseFactory {

    private static FindReservationResponse create() {
      return FindReservationResponse.builder()
          .swimmingPool(
              FindReservationResponse.SwimmingPool.builder()
                  .id(1L)
                  .name("DUMMY_POOL_NAME")
                  .accountNo("DUMMY_ACCOUNT_NO")
                  .build()
          )
          .swimmingClass(
              FindReservationResponse.SwimmingClass.builder()
                  .id(2L)
                  .month(5)
                  .type("DUMMY_CLASS_TYPE")
                  .subType("DUMMY_CLASS_SUB_TYPE")
                  .days(java.util.List.of("월", "수", "금"))
                  .startTime(LocalTime.of(10, 0))
                  .endTime(LocalTime.of(11, 0))
                  .build()
          )
          .ticket(
              FindReservationResponse.Ticket.builder()
                  .id(3L)
                  .name("DUMMY_TICKET_NAME")
                  .price(50000)
                  .build()
          )
          .reservation(
              FindReservationResponse.Reservation.builder()
                  .id(4L)
                  .reservedAt(LocalDateTime.of(2025, 4, 1, 10, 0))
                  .build()
          )
          .payment(
              FindReservationResponse.Payment.builder()
                  .method("DUMMY_PAYMENT_METHOD")
                  .build()
          )
          .build();
    }
  }
}