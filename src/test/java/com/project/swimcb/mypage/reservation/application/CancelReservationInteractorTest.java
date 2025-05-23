package com.project.swimcb.mypage.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.CancellationReason.USER_CANCELLED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.project.swimcb.swimmingpool.domain.Reservation;
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CancelReservationInteractorTest {

  @InjectMocks
  private CancelReservationInteractor interactor;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private BoCancelReservationDsGateway boCancelReservationDsGateway;

  private Long memberId;
  private Long reservationId;

  @BeforeEach
  void setUp() {
    memberId = 1L;
    reservationId = 1L;
  }

  @Test
  @DisplayName("취소로 변경이 가능한 예약이면, 사용자 취소를 사유로 취소 처리한다")
  void shouldCancelWithUserCancelled_WhenCanTransitionToCancel() {
    // given
    val reservation = mock(Reservation.class);
    val swimmingClassId = 1L;

    when(reservationRepository.findByIdAndMemberId(reservationId, memberId))
        .thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToCancelByUser()).thenReturn(true);
    when(boCancelReservationDsGateway.findSwimmingClassByReservationId(reservationId))
        .thenReturn(swimmingClassId);

    // when
    interactor.cancelReservation(memberId, reservationId);

    // then
    verify(reservationRepository, only()).findByIdAndMemberId(reservationId, memberId);
    verify(reservation, times(1)).canTransitionToCancelByUser();
    verify(reservation, times(1)).cancel(USER_CANCELLED);
    verify(boCancelReservationDsGateway, times(1)).findSwimmingClassByReservationId(reservationId);
    verify(boCancelReservationDsGateway, times(1))
        .updateSwimmingClassReservedCount(swimmingClassId, -1);
  }

  @Test
  @DisplayName("존재하지 않는 예약 취소시 예외를 발생시킨다")
  void cancelReservation_whenReservationNotFound_shouldThrowException() {
    // given
    val reservationId = 999L;

    when(reservationRepository.findByIdAndMemberId(reservationId, memberId))
        .thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> interactor.cancelReservation(memberId, reservationId))
        .isInstanceOf(NoSuchElementException.class);

    verify(reservationRepository, only()).findByIdAndMemberId(reservationId, memberId);
  }

  @Test
  @DisplayName("memberId가 null이면 NullPointerException을 발생시킨다")
  void cancelReservation_whenMemberIdIsNull_shouldThrowException() {
    // given
    // when
    // then
    assertThatThrownBy(() -> interactor.cancelReservation(null, reservationId))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("reservationId가 null이면 NullPointerException을 발생시킨다")
  void cancelReservation_whenReservationIdIsNull_shouldThrowException() {
    // given
    // when
    // then
    assertThatThrownBy(() -> interactor.cancelReservation(memberId, null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("취소로 변경이 불가능한 예약이면, 예외가 발생한다")
  void shouldThrowException_WhenCannotTransitionToCancel() {
    // given
    val reservation = mock(Reservation.class);

    when(reservationRepository.findByIdAndMemberId(anyLong(), anyLong())).thenReturn(
        Optional.of(reservation));
    when(reservation.canTransitionToCancelByUser()).thenReturn(false);

    // when
    // then
    assertThatThrownBy(() -> interactor.cancelReservation(memberId, reservationId))
        .isInstanceOf(IllegalStateException.class);
  }

}
