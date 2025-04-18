package com.project.swimcb.mypage.reservation.adapter.in;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_COMPLETED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.mypage.reservation.application.port.in.FindReservationDetailUseCase;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
                  .phone("010-1234-5678")
                  .imagePath("images/pool/1.jpg")
                  .accountNo(AccountNo.of("123-456-7890"))
                  .build()
          )
          .swimmingClass(
              ReservationDetail.SwimmingClass.builder()
                  .id(2L)
                  .month(3)
                  .type(SwimmingClassTypeName.GROUP)
                  .subType("초급반")
                  .daysOfWeek(new ClassDayOfWeek(List.of()))
                  .startTime(LocalTime.of(10, 0))
                  .endTime(LocalTime.of(11, 0))
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
                  .status(PAYMENT_COMPLETED)
                  .reservedAt(LocalDateTime.of(2023, 4, 1, 10, 0, 0))
                  .build()
          )
          .payment(
              ReservationDetail.Payment.builder()
                  .method(CASH_ON_SITE)
                  .amount(50000)
                  .build()
          )
          .cancel(
              ReservationDetail.Cancel.builder().build()
          )
          .refund(
              ReservationDetail.Refund.builder().build()
          )
          .review(
              ReservationDetail.Review.builder().build()
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
                  .phone("DUMMY_POOL_PHONE")
                  .imageUrl("DUMMY_POOL_IMAGE_URL")
                  .accountNo("DUMMY_ACCOUNT_NO")
                  .build()
          )
          .swimmingClass(
              FindReservationDetailResponse.SwimmingClass.builder()
                  .id(2L)
                  .month(3)
                  .type("DUMMY_CLASS_TYPE")
                  .subType("DUMMY_CLASS_SUB_TYPE")
                  .days(List.of("월", "수", "금"))
                  .startTime(LocalTime.of(10, 0))
                  .endTime(LocalTime.of(11, 0))
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
          .cancel(
              FindReservationDetailResponse.Cancel.builder().build()
          )
          .refund(
              FindReservationDetailResponse.Refund.builder().build()
          )
          .payment(
              FindReservationDetailResponse.Payment.builder()
                  .method("DUMMY_PAYMENT_METHOD")
                  .amount(50000)
                  .pendingAt(LocalDateTime.of(2023, 4, 1, 10, 0, 0))
                  .approvedAt(null)
                  .build()
          )
          .review(
              FindReservationDetailResponse.Review.builder().build()
          )
          .build();
    }
  }
}