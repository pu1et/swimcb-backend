package com.project.swimcb.mypage.reservation.adapter.out;

import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.TicketType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.swimcb.mypage.reservation.adapter.out.FindReservationsDataMapper.QueryReservation;
import com.project.swimcb.mypage.reservation.adapter.out.FindReservationsDataMapper.WaitingReservation;
import com.project.swimcb.mypage.reservation.domain.Reservation;
import com.project.swimcb.mypage.reservation.domain.Reservation.ReservationDetail;
import com.project.swimcb.mypage.reservation.domain.Reservation.SwimmingClass;
import com.project.swimcb.mypage.reservation.domain.Reservation.SwimmingPool;
import com.project.swimcb.mypage.reservation.domain.Reservation.Ticket;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class FindReservationsDataMapperTest {

  @InjectMocks
  private FindReservationsDataMapper findReservationsDataMapper;

  @Mock
  private JPAQueryFactory queryFactory;

  @Nested
  @DisplayName("setCurrentWaitingNo 메서드 테스트")
  class SetCurrentWaitingNoTest {

    @Test
    @DisplayName("대기 예약 번호가 정상적으로 계산되어야 한다")
    void shouldCalculateCurrentWaitingNumbers() {
      // given
      val findReservationsDataMapper = spy(new FindReservationsDataMapper(queryFactory));
      val reservation1 = create(3L, 1L, RESERVATION_PENDING, 5);
      val reservation2 = create(5L, 2L, PAYMENT_PENDING, null);
      val reservation3 = create(10L, 3L, RESERVATION_PENDING, 5);

      val reservations = List.of(reservation1, reservation2, reservation3);

      val mockRelatedReservations = List.of(
          new WaitingReservation(1L, 1, RESERVATION_PENDING, 1L),
          new WaitingReservation(2L, 2, RESERVATION_PENDING, 1L),
          new WaitingReservation(3L, 3, RESERVATION_PENDING, 1L),

          new WaitingReservation(6L, 1, RESERVATION_PENDING, 3L),
          new WaitingReservation(7L, 1, RESERVATION_PENDING, 3L),
          new WaitingReservation(8L, 1, RESERVATION_PENDING, 3L),
          new WaitingReservation(9L, 1, RESERVATION_PENDING, 3L)
      );

      doReturn(mockRelatedReservations)
          .when(findReservationsDataMapper)
          .findRelatedReservationPendingReservations(anySet());

      // when
      val result = findReservationsDataMapper.setCurrentWaitingNo(reservations);

      // then
      assertThat(result).hasSize(3);

      // 첫번째 예약은 대기 세번째
      assertThat(result.get(0).reservationDetail().waitingNo()).isEqualTo(3);

      // 두번째 예약은 대기 상태가 아니므로 null
      assertThat(result.get(1).reservationDetail().waitingNo()).isNull();

      // 세번째 예약은 대기 두번째
      assertThat(result.get(2).reservationDetail().waitingNo()).isEqualTo(5);

      verify(findReservationsDataMapper, times(1)).findRelatedReservationPendingReservations(Set.of(1L, 3L));
    }

    @Test
    @DisplayName("회원의 예약내역 중 대기 예약이 없는 경우 원본 목록을 그대로 반환해야 한다")
    void shouldReturnOriginalListWhenNoWaitingReservations() {
      // given
      val findReservationsDataMapper = spy(new FindReservationsDataMapper(queryFactory));
      val reservation1 = create(1L, 1L, PAYMENT_PENDING, null);
      val reservation2 = create(2L, 1L, PAYMENT_PENDING, null);

      val reservations = List.of(reservation1, reservation2);

      // when
      val result = findReservationsDataMapper.setCurrentWaitingNo(reservations);

      // then
      assertThat(result).isEqualTo(reservations);

      verify(findReservationsDataMapper, never()).findRelatedReservationPendingReservations(Set.of(1L));
    }

  }

  private static Reservation create(Long id, Long swimmingClassId, ReservationStatus status,
      Integer waitingNo) {
    return Reservation.builder()
        .swimmingPool(createSwimmingPool())
        .swimmingClass(createSwimmingClass(swimmingClassId))
        .ticket(createTicket())
        .reservationDetail(ReservationDetail.builder()
            .id(id)
            .ticketType(SWIMMING_CLASS)
            .status(status)
            .reservedAt(LocalDateTime.now())
            .waitingNo(waitingNo)
            .build())
        .build();
  }

  private static SwimmingPool createSwimmingPool() {
    return SwimmingPool.builder()
        .id(1L)
        .name("DUMMY_POOL_NAME")
        .imagePath("DUMMY_POOL_IMAGE_PATH")
        .build();
  }

  private static SwimmingClass createSwimmingClass(Long id) {
    return SwimmingClass.builder()
        .id(id)
        .month(5)
        .type(SwimmingClassTypeName.GROUP)
        .subType("DUMMY_SUB_TYPE")
        .daysOfWeek(ClassDayOfWeek.of(0b1111111))
        .startTime(LocalTime.of(10, 0))
        .endTime(LocalTime.of(11, 0))
        .isCanceled(false)
        .build();
  }

  private static Ticket createTicket() {
    return Ticket.builder()
        .id(1L)
        .name("DUMMY_TICKET_NAME")
        .price(100000)
        .build();
  }

}
