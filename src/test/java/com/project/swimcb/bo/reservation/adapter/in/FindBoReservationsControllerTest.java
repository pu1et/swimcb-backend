package com.project.swimcb.bo.reservation.adapter.in;

import static com.project.swimcb.bo.reservation.adapter.in.FindBoReservationsControllerTest.SWIMMING_POOL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.reservation.application.port.in.FindBoReservationsUseCase;
import com.project.swimcb.bo.reservation.domain.BoReservation;
import com.project.swimcb.bo.reservation.domain.BoReservation.Member;
import com.project.swimcb.bo.reservation.domain.BoReservation.Payment;
import com.project.swimcb.bo.reservation.domain.BoReservation.ReservationDetail;
import com.project.swimcb.bo.reservation.domain.BoReservation.SwimmingClass;
import com.project.swimcb.bo.reservation.domain.FindBoReservationsCondition;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindBoReservationsController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class FindBoReservationsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindBoReservationsUseCase useCase;

  @MockitoBean
  private FindBoReservationsResponseMapper mapper;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/reservations";

  @Nested
  @DisplayName("예약 및 결제 리스트 조회 API는")
  class FindReservationsWithPayments {

    @Test
    @DisplayName("필수 파라미터와 함께 요청했을 때 성공적으로 응답한다")
    void returnSuccessWithRequiredParameters() throws Exception {
      // given
      val startDate = LocalDate.of(2025, 1, 1);
      val endDate = LocalDate.of(2025, 12, 31);
      val page = 1;
      val size = 10;

      val boReservationsPage = TestBoReservationsFactory.create();
      when(useCase.findBoReservations(any())).thenReturn(boReservationsPage);

      val response = TestFindBoReservationsResponseFactory.create();
      when(mapper.toResponse(boReservationsPage)).thenReturn(response);

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("startDate", startDate.toString())
              .param("endDate", endDate.toString())
              .param("page", String.valueOf(page))
              .param("size", String.valueOf(size)))
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(response)));

      verify(useCase, only()).findBoReservations(assertArg(i -> {
        assertThat(i.startDate()).isEqualTo(startDate);
        assertThat(i.endDate()).isEqualTo(endDate);
        assertThat(i.pageable().getPageNumber()).isEqualTo(page - 1);
        assertThat(i.pageable().getPageSize()).isEqualTo(size);
      }));
    }

    @Test
    @DisplayName("모든 선택적 파라미터와 함께 요청했을 때 성공적으로 응답한다")
    void returnSuccessWithAllOptionalParameters() throws Exception {
      // given
      val startDate = LocalDate.of(2025, 1, 1);
      val endDate = LocalDate.of(2025, 12, 31);
      val programType = TicketType.SWIMMING_CLASS;
      val reservationStatus = ReservationStatus.PAYMENT_COMPLETED;
      val paymentMethod = PaymentMethod.CASH_ON_SITE;
      val page = 1;
      val size = 10;

      val boReservationsPage = TestBoReservationsFactory.create();
      when(useCase.findBoReservations(any())).thenReturn(boReservationsPage);

      val response = TestFindBoReservationsResponseFactory.create();
      when(mapper.toResponse(boReservationsPage)).thenReturn(response);

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("startDate", startDate.toString())
              .param("endDate", endDate.toString())
              .param("programType", programType.name())
              .param("reservationStatus", reservationStatus.name())
              .param("paymentMethod", paymentMethod.name())
              .param("page", String.valueOf(page))
              .param("size", String.valueOf(size)))
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(response)));

      verify(useCase, only()).findBoReservations(any(FindBoReservationsCondition.class));
    }

    @Test
    @DisplayName("페이지 번호가 1 미만일 경우 400 에러를 반환한다")
    void return400WhenPageIsLessThan1() throws Exception {
      // given
      val startDate = LocalDate.of(2025, 1, 1);
      val endDate = LocalDate.of(2025, 12, 31);
      val invalidPage = 0;
      val size = 10;

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("startDate", startDate.toString())
              .param("endDate", endDate.toString())
              .param("page", String.valueOf(invalidPage))
              .param("size", String.valueOf(size)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("페이지 크기가 1 미만일 경우 400 에러를 반환한다")
    void return400WhenSizeIsLessThan1() throws Exception {
      // given
      val startDate = LocalDate.of(2025, 1, 1);
      val endDate = LocalDate.of(2025, 12, 31);
      val page = 1;
      val invalidSize = 0;

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("startDate", startDate.toString())
              .param("endDate", endDate.toString())
              .param("page", String.valueOf(page))
              .param("size", String.valueOf(invalidSize)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("시작일을 입력하지 않으면 400 에러를 반환한다")
    void return400WhenStartDateIsMissing() throws Exception {
      // given
      val endDate = LocalDate.of(2025, 12, 31);
      val page = 1;
      val size = 10;

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("endDate", endDate.toString())
              .param("page", String.valueOf(page))
              .param("size", String.valueOf(size)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("종료일을 입력하지 않으면 400 에러를 반환한다")
    void return400WhenEndDateIsMissing() throws Exception {
      // given
      val startDate = LocalDate.of(2025, 1, 1);
      val page = 1;
      val size = 10;

      // when & then
      mockMvc.perform(get(PATH)
              .param("startDate", startDate.toString())
              .param("page", String.valueOf(page))
              .param("size", String.valueOf(size)))
          .andExpect(status().isBadRequest());
    }
  }

  private static class TestBoReservationsFactory {

    private static PageImpl<BoReservation> create() {
      val reservation = BoReservation.builder()
          .member(Member.builder()
              .id(1L)
              .name("DUMMY_MEMBER_NAME")
              .birthDate(LocalDate.of(1990, 1, 1))
              .build())
          .swimmingClass(SwimmingClass.builder()
              .id(101L)
              .type(SwimmingClassTypeName.GROUP)
              .subType("DUMMY_CLASS_SUB_TYPE")
              .daysOfWeek(ClassDayOfWeek.of(3)) // 수요일
              .startTime(LocalTime.of(10, 0))
              .endTime(LocalTime.of(11, 0))
              .build())
          .reservationDetail(ReservationDetail.builder()
              .id(1001L)
              .ticketType(TicketType.SWIMMING_CLASS)
              .status(ReservationStatus.PAYMENT_COMPLETED)
              .reservedAt(LocalDateTime.now().minusDays(5))
              .paymentPendingAt(LocalDateTime.now().minusDays(5))
              .paymentCompletedAt(LocalDateTime.now().minusDays(4))
              .build())
          .payment(Payment.builder()
              .method(PaymentMethod.CASH_ON_SITE)
              .amount(50000)
              .build())
          .build();

      return new PageImpl<>(
          List.of(reservation),
          PageRequest.of(0, 10),
          1
      );
    }
  }

  private static class TestFindBoReservationsResponseFactory {

    private static FindBoReservationsResponse create() {
      val reservationResponse = new FindBoReservationsResponse.BoReservation(
          new FindBoReservationsResponse.Member(
              1L,
              "DUMMY_MEMBER_NAME",
              LocalDate.of(1990, 1, 1)
          ),
          new FindBoReservationsResponse.SwimmingClass(
              101L,
              "DUMMY_CLASS_TYPE_NAME",
              "DUMMY_CLASS_SUB_TYPE",
              ClassDayOfWeek.of(3).toDays(),
              LocalTime.of(10, 0),
              LocalTime.of(11, 0)
          ),
          new FindBoReservationsResponse.ReservationDetail(
              1001L,
              TicketType.SWIMMING_CLASS,
              "DUMMY_RESERVATION_STATUS",
              null,
              LocalDateTime.now().minusDays(5),
              LocalDateTime.now().minusDays(4)
          ),
          new FindBoReservationsResponse.Payment(
              "DUMMY_PAYMENT_METHOD",
              50000
          ),
          null,
          null
      );

      return new FindBoReservationsResponse(
          new PageImpl<>(
              List.of(reservationResponse),
              PageRequest.of(0, 10),
              1
          )
      );
    }
  }
}
