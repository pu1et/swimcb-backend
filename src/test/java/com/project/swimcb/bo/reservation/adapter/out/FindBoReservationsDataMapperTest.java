package com.project.swimcb.bo.reservation.adapter.out;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.BANK_TRANSFER;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_VERIFICATION;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_CANCELLED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.TicketType.SWIMMING_CLASS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.project.swimcb.bo.reservation.adapter.out.FindBoReservationsDataMapper.WaitingReservation;
import com.project.swimcb.bo.reservation.domain.BoReservation;
import com.project.swimcb.bo.reservation.domain.BoReservation.Member;
import com.project.swimcb.bo.reservation.domain.BoReservation.Payment;
import com.project.swimcb.bo.reservation.domain.BoReservation.ReservationDetail;
import com.project.swimcb.bo.reservation.domain.BoReservation.SwimmingClass;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindBoReservationsDataMapperTest {

  @InjectMocks
  private FindBoReservationsDataMapper mapper;

  @Mock
  private JPAQueryFactory queryFactory;

  @Test
  @DisplayName("대기 예약의 waitingNo가 정렬 순서대로 재할당되어야 한다")
  void updateWithCurrentWaitingNo_ShouldUpdateWaitingNumbersCorrectly() {
    // given
    val swimmingPoolId = 1L;

    // 테스트용 예약 데이터 생성
    val reservation1 = createReservation(1L, 1L, RESERVATION_PENDING, 5);
    val reservation2 = createReservation(2L, 1L, RESERVATION_PENDING, 7);
    val reservation3 = createReservation(3L, 1L, PAYMENT_PENDING, null);
    val reservation4 = createReservation(4L, 1L, RESERVATION_PENDING, 8);

    val reservation5 = createReservation(5L, 2L, RESERVATION_PENDING, 2);
    val reservation6 = createReservation(6L, 2L, RESERVATION_PENDING, 5);
    val reservation7 = createReservation(7L, 2L, PAYMENT_PENDING, null);

    val reservation8 = createReservation(8L, 3L, PAYMENT_COMPLETED, null);
    val reservation9 = createReservation(9L, 3L, PAYMENT_VERIFICATION, null);

    val reservations = List.of(
        reservation1, reservation2, reservation3, reservation4, reservation5, reservation6,
        reservation7, reservation8, reservation9
    );

    // 조회할 클래스 ID
    val expectedClassIds = Set.of(1L, 2L);

    // findAllWaitingReservationsByClasses 메서드가 반환할 대기 예약 목록
    val waitingReservations = List.of(
        new WaitingReservation(1L, 1, 1L),
        new WaitingReservation(2L, 2, 1L),
        new WaitingReservation(4L, 3, 1L),

        new WaitingReservation(5L, 1, 2L),
        new WaitingReservation(6L, 2, 2L)
    );

    // mock 메서드 설정
    val spyMapper = spy(mapper);

    doReturn(waitingReservations)
        .when(spyMapper)
        .findAllWaitingReservationsByClasses(swimmingPoolId, expectedClassIds);

    // when
    val result = spyMapper.updateWithCurrentWaitingNo(reservations, swimmingPoolId);

    // then
    assertThat(result).hasSize(9);

    // 클래스 1의 대기 예약: 원래 waitingNo 5인 예약은 1번, 7인 예약은 2번, 8인 예약은 3번이 됨
    assertThat(findReservationById(result, 1L).reservationDetail().waitingNo()).isEqualTo(1);
    assertThat(findReservationById(result, 2L).reservationDetail().waitingNo()).isEqualTo(2);
    assertThat(findReservationById(result, 3L).reservationDetail().waitingNo()).isNull();
    assertThat(findReservationById(result, 4L).reservationDetail().waitingNo()).isEqualTo(3);

    // 클래스 2의 대기 예약: 원래 waitingNo 2인 예약은 1번, waitingNo 5인 예약은 2번이 됨
    assertThat(findReservationById(result, 5L).reservationDetail().waitingNo()).isEqualTo(1);
    assertThat(findReservationById(result, 6L).reservationDetail().waitingNo()).isEqualTo(2);
    assertThat(findReservationById(result, 7L).reservationDetail().waitingNo()).isNull();

    // 클래스 3의 대기 예약은 없음
    // 대기 상태가 아닌 예약은 waitingNo가 null로 유지됨
    assertThat(findReservationById(result, 8L).reservationDetail().waitingNo()).isNull();
    assertThat(findReservationById(result, 9L).reservationDetail().waitingNo()).isNull();
  }

  @Test
  @DisplayName("대기 예약이 없을 경우 원본 리스트가 그대로 반환되어야 한다")
  void updateWithCurrentWaitingNo_WithEmptyPendingReservations_ShouldReturnOriginalList() {
    // given
    val swimmingPoolId = 1L;

    // 대기 상태가 아닌 예약만 있는 경우
    val reservations = List.of(
        createReservation(1L, 1L, PAYMENT_COMPLETED, null),
        createReservation(2L, 1L, RESERVATION_CANCELLED, null)
    );

    // when
    val result = mapper.updateWithCurrentWaitingNo(reservations, swimmingPoolId);

    // then
    assertEquals(reservations, result);
  }

  private BoReservation createReservation(Long reservationId, Long classId,
      ReservationStatus status, Integer waitingNo) {
    return BoReservation.builder()
        .member(Member.builder().id(1L).name("테스트").birthDate(LocalDate.now()).build())
        .swimmingClass(SwimmingClass.builder()
            .id(classId)
            .type(SwimmingClassTypeName.GROUP)
            .subType("DUMMY_SUB_TYPE")
            .daysOfWeek(ClassDayOfWeek.of(0b1111111))
            .startTime(LocalTime.of(10, 0))
            .endTime(LocalTime.of(11, 0))
            .build())
        .reservationDetail(ReservationDetail.builder()
            .id(reservationId)
            .ticketType(SWIMMING_CLASS)
            .status(status)
            .waitingNo(waitingNo)
            .reservedAt(LocalDateTime.now())
            .build())
        .payment(
            Payment.builder()
                .method(BANK_TRANSFER)
                .amount(10000)
                .build()
        )
        .build();
  }

  private BoReservation findReservationById(List<BoReservation> reservations, Long id) {
    return reservations.stream()
        .filter(r -> r.reservationDetail().id() == id)
        .findFirst()
        .orElseThrow();
  }

}
