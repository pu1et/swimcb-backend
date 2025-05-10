package com.project.swimcb.mypage.reservation.application;

import static java.time.DayOfWeek.MONDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.mypage.reservation.application.port.out.FindReservationsDsGateway;
import com.project.swimcb.mypage.reservation.domain.Reservation;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FindReservationsInteractorTest {

  @InjectMocks
  private FindReservationsInteractor interactor;

  @Mock
  private FindReservationsDsGateway gateway;

  @Nested
  @DisplayName("예약 목록 조회 시")
  class WhenFindingReservations {

    @Test
    @DisplayName("회원 ID와 페이징 정보로 예약 목록을 조회한다")
    void shouldReturnReservationsByMemberId() {
      // given
      val memberId = 1L;
      val pageable = PageRequest.of(0, 10);

      val reservation = createReservation();
      val expectedPage = new PageImpl<>(List.of(reservation), pageable, 1);

      when(gateway.findReservations(memberId, pageable)).thenReturn(expectedPage);

      // when
      val result = interactor.findReservations(memberId, pageable);

      // then
      assertThat(result).isEqualTo(expectedPage);

      verify(gateway, only()).findReservations(memberId, pageable);
    }

    @Test
    @DisplayName("결과가 없으면 빈 페이지를 반환한다")
    void shouldReturnEmptyResults() {
      // given
      val memberId = 1L;
      val pageable = PageRequest.of(0, 10);

      val emptyPage = new PageImpl<Reservation>(List.of(), pageable, 0);

      when(gateway.findReservations(memberId, pageable)).thenReturn(emptyPage);

      // when
      val result = interactor.findReservations(memberId, pageable);

      // then
      assertThat(result).isEmpty();
      assertThat(result.getTotalElements()).isZero();

      verify(gateway, only()).findReservations(memberId, pageable);
    }

    @Test
    @DisplayName("페이지 정보가 없으면 예외가 발생한다")
    void shouldThrowExceptionWhenPageableIsNull() {
      // given
      val memberId = 1L;

      // when & then
      assertThatThrownBy(() -> interactor.findReservations(memberId, null))
          .isInstanceOf(NullPointerException.class);
    }
  }

  private Reservation createReservation() {
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
        .review(
            Reservation.Review.builder()
                .id(5L)
                .build()
        )
        .build();
  }
}
