package com.project.swimcb.mypage.reservation.adapter.in;

import static com.project.swimcb.mypage.reservation.adapter.in.FindReservationControllerTest.MEMBER_ID;
import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.*;
import static java.time.DayOfWeek.MONDAY;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.mypage.reservation.application.port.in.FindReservationsUseCase;
import com.project.swimcb.mypage.reservation.domain.Reservation;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindReservationController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class FindReservationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindReservationsUseCase useCase;

  @MockitoBean
  private FindReservationsResponseMapper mapper;

  static final long MEMBER_ID = 1L;

  private static final String PATH = "/api/my-page/reservations";

  @Nested
  @DisplayName("예약 목록 조회 API는")
  class FindReservations {

    @Test
    @DisplayName("예약 목록을 정상적으로 반환한다")
    void returnReservationsWhenRequested() throws Exception {
      // given
      val pageable = PageRequest.of(0, 10);

      val reservation = TestReservationFactory.create();
      val reservationsPage = new PageImpl<>(List.of(reservation), pageable, 1);

      val response = TestFindReservationsResponseFactory.create();

      when(useCase.findReservations(MEMBER_ID, pageable)).thenReturn(reservationsPage);
      when(mapper.toResponse(reservationsPage)).thenReturn(response);

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("page", "1")
              .param("size", "10"))
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(response)));

      verify(useCase, only()).findReservations(MEMBER_ID, pageable);
      verify(mapper, only()).toResponse(reservationsPage);
    }

    @Test
    @DisplayName("기본 페이지 파라미터로 요청할 경우 기본값을 사용한다")
    void useDefaultParametersWhenNotProvided() throws Exception {
      // given
      val memberId = 1L;
      val defaultPageable = PageRequest.of(0, 10); // 컨트롤러의 기본값

      final Page<Reservation> emptyPage = new PageImpl<>(List.of(), defaultPageable, 0);
      val emptyResponse = TestFindReservationsResponseFactory.createEmpty();

      when(useCase.findReservations(memberId, defaultPageable)).thenReturn(emptyPage);
      when(mapper.toResponse(emptyPage)).thenReturn(emptyResponse);

      // when
      // then
      mockMvc.perform(get(PATH))
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(emptyResponse)));

      verify(useCase, only()).findReservations(memberId, defaultPageable);
      verify(mapper, only()).toResponse(emptyPage);
    }
  }

  private static class TestReservationFactory {

    private static Reservation create() {
      return Reservation.builder()
          .swimmingPool(
              Reservation.SwimmingPool.builder()
                  .id(1L)
                  .name("DUMMY_POOL_NAME")
                  .imagePath("DUMMY_IMAGE_PATH")
                  .build()
          )
          .swimmingClass(
              Reservation.SwimmingClass.builder()
                  .id(2L)
                  .month(7)
                  .type(SwimmingClassTypeName.GROUP)
                  .subType("DUMMY_CLASS_SUB_TYPE")
                  .daysOfWeek(new ClassDayOfWeek(List.of(MONDAY)))
                  .startTime(LocalTime.of(18, 0))
                  .endTime(LocalTime.of(19, 0))
                  .isCanceled(false)
                  .build()
          )
          .ticket(
              Reservation.Ticket.builder()
                  .id(3L)
                  .name("DUMMY_TICKET_NAME")
                  .price(10000)
                  .build()
          )
          .reservationDetail(
              Reservation.ReservationDetail.builder()
                  .id(4L)
                  .ticketType(TicketType.SWIMMING_CLASS)
                  .status(ReservationStatus.PAYMENT_PENDING)
                  .reservedAt(LocalDateTime.of(2025, 1, 1, 1, 1))
                  .waitingNo(null)
                  .build()
          )
          .payment(
              Reservation.Payment.builder()
                  .method(CASH_ON_SITE)
                  .pendingAt(LocalDateTime.of(2025, 1, 1, 1, 1))
                  .build()
          )
          .review(
              Reservation.Review.builder()
                  .id(5L)
                  .build()
          )
          .build();
    }
  }

  private static class TestFindReservationsResponseFactory {

    static FindReservationsResponse create() {
      val swimmingPool = FindReservationsResponse.SwimmingPool.builder()
          .id(1L)
          .name("DUMMY_POOL_NAME")
          .imageUrl("DUMMY_IMAGE_URL")
          .build();

      val swimmingClass = FindReservationsResponse.SwimmingClass.builder()
          .id(2L)
          .month(7)
          .type("DUMMY_CLASS_TYPE")
          .subType("DUMMY_CLASS_SUB_TYPE")
          .days(List.of("월", "수", "금"))
          .startTime(LocalTime.of(18, 0))
          .endTime(LocalTime.of(19, 0))
          .isCanceled(false)
          .build();

      val ticket = FindReservationsResponse.Ticket.builder()
          .id(3L)
          .name("DUMMY_TICKET_NAME")
          .price(150000)
          .build();

      val reservationInfo = FindReservationsResponse.ReservationInfo.builder()
          .id(4L)
          .ticketType(TicketType.SWIMMING_CLASS)
          .status("결제대기")
          .reservedAt(LocalDateTime.of(2025, 1, 1, 1, 1))
          .waitingNo(null)
          .build();

      val payment = FindReservationsResponse.Payment.builder()
          .method(PaymentMethod.CASH_ON_SITE.getDescription())
          .pendingAt(LocalDateTime.of(2025, 1, 1, 1, 1))
          .build();

      val review = FindReservationsResponse.Review.builder()
          .id(5L)
          .build();

      val responseReservation = FindReservationsResponse.Reservation.builder()
          .swimmingPool(swimmingPool)
          .swimmingClass(swimmingClass)
          .ticket(ticket)
          .reservationInfo(reservationInfo)
          .payment(payment)
          .review(review)
          .build();

      val page = new PageImpl<>(List.of(responseReservation), PageRequest.of(0, 10), 1);

      return new FindReservationsResponse(page);
    }

    static FindReservationsResponse createEmpty() {
      final Page<FindReservationsResponse.Reservation> emptyPage = new PageImpl<>(List.of(),
          PageRequest.of(1, 10), 0);
      return new FindReservationsResponse(emptyPage);
    }
  }
}
