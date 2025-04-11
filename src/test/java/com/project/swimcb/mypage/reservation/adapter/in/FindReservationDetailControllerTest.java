package com.project.swimcb.mypage.reservation.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.mypage.reservation.application.port.in.FindReservationDetailUseCase;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail;
import java.time.LocalDateTime;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindReservationDetailController.class)
class FindReservationDetailControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindReservationDetailUseCase useCase;

  @MockitoBean
  private FindReservationDetailResponseMapper mapper;

  private static final String PATH = "/api/my-page/reservations/{reservationId}";

  @Nested
  @DisplayName("마이페이지 - 예약 상세 정보 조회 API는")
  class FindReservationDetail {

    @Test
    @DisplayName("유효한 예약 ID로 요청 시 예약 상세 정보를 응답한다")
    void returnReservationDetailWhenValidIdProvided() throws Exception {
      // given
      val reservationId = 1L;

      val detail = TestReservationDetailFactory.create();
      val response = TestFindReservationDetailResponseFactory.create();

      when(useCase.findReservationDetail(anyLong())).thenReturn(detail);
      when(mapper.toResponse(any())).thenReturn(response);

      // when
      // then
      mockMvc.perform(get(PATH, reservationId))
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(response)));

      verify(useCase).findReservationDetail(reservationId);
      verify(mapper).toResponse(detail);
    }
  }

  private static class TestReservationDetailFactory {

    private static ReservationDetail create() {
      return ReservationDetail.builder()
          .swimmingPool(
              ReservationDetail.SwimmingPool.builder()
                  .id(1L)
                  .name("테스트 수영장")
                  .imagePath("images/pool/1.jpg")
                  .build()
          )
          .swimmingClass(
              ReservationDetail.SwimmingClass.builder()
                  .id(2L)
                  .type(com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP)
                  .subType("초급반")
                  .build()
          )
          .ticket(
              ReservationDetail.Ticket.builder()
                  .id(3L)
                  .name("1개월 이용권")
                  .price(50000)
                  .build()
          )
          .reservation(
              ReservationDetail.Reservation.builder()
                  .id(1L)
                  .status(
                      com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_COMPLETED)
                  .reservedAt(LocalDateTime.of(2023, 4, 1, 10, 0, 0))
                  .build()
          )
          .payment(
              ReservationDetail.Payment.builder()
                  .method(com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE)
                  .amount(50000)
                  .build()
          )
          .build();
    }
  }

  private static class TestFindReservationDetailResponseFactory {

    private static FindReservationDetailResponse create() {
      return FindReservationDetailResponse.builder()
          .swimmingPool(
              FindReservationDetailResponse.SwimmingPool.builder()
                  .id(1L)
                  .name("DUMMY_POOL_NAME")
                  .imageUrl("DUMMY_POOL_IMAGE_URL")
                  .build()
          )
          .swimmingClass(
              FindReservationDetailResponse.SwimmingClass.builder()
                  .id(2L)
                  .type("DUMMY_CLASS_TYPE")
                  .subType("DUMMY_CLASS_SUB_TYPE")
                  .build()
          )
          .ticket(
              FindReservationDetailResponse.Ticket.builder()
                  .id(3L)
                  .name("DUMMY_TICKET_NAME")
                  .price(50000)
                  .build()
          )
          .reservation(
              FindReservationDetailResponse.Reservation.builder()
                  .id(1L)
                  .status("DUMMY_RESERVATION_STATUS")
                  .reservedAt(LocalDateTime.of(2023, 4, 1, 10, 0, 0))
                  .build()
          )
          .payment(
              FindReservationDetailResponse.Payment.builder()
                  .method("DUMMY_PAYMENT_METHOD")
                  .amount(50000)
                  .requestedAt(LocalDateTime.of(2023, 4, 1, 10, 0, 0))
                  .build()
          )
          .build();
    }
  }
}