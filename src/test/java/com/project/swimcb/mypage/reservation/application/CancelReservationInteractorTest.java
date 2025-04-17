package com.project.swimcb.mypage.reservation.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.swimmingpool.domain.Reservation;
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
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

  @Test
  @DisplayName("예약 취소시 예약을 찾아 취소 상태로 변경한다")
  void cancelReservation_shouldCancelReservation() {
    // given
    val memberId = 1L;
    val reservationId = 2L;

    val reservation = mock(Reservation.class);

    when(reservationRepository.findByIdAndMemberId(reservationId, memberId))
        .thenReturn(Optional.of(reservation));

    // when
    interactor.cancelReservation(memberId, reservationId);

    // then
    verify(reservationRepository, only()).findByIdAndMemberId(reservationId, memberId);
    verify(reservation, only()).cancel();
  }

  @Test
  @DisplayName("존재하지 않는 예약 취소시 예외를 발생시킨다")
  void cancelReservation_whenReservationNotFound_shouldThrowException() {
    // given
    val memberId = 1L;
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
    val reservationId = 1L;

    // when
    // then
    assertThatThrownBy(() -> interactor.cancelReservation(null, reservationId))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("reservationId가 null이면 NullPointerException을 발생시킨다")
  void cancelReservation_whenReservationIdIsNull_shouldThrowException() {
    // given
    val memberId = 1L;

    // when
    // then
    assertThatThrownBy(() -> interactor.cancelReservation(memberId, null))
        .isInstanceOf(NullPointerException.class);
  }
}