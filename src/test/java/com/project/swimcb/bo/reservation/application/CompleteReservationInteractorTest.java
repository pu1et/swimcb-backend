package com.project.swimcb.bo.reservation.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
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
class CompleteReservationInteractorTest {

  @InjectMocks
  private CompleteReservationInteractor interactor;

  @Mock
  private ReservationRepository reservationRepository;

  @Test
  @DisplayName("예약이 존재하는 경우 예약 완료를 성공적으로 처리한다")
  void shouldCompleteReservationSuccessfully() {
    // given
    val reservationId = 1L;
    val reservation = mock(Reservation.class);

    when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToComplete()).thenReturn(true);

    // when
    interactor.completeReservation(reservationId);

    // then
    verify(reservationRepository, only()).findById(reservationId);
    verify(reservation, times(1)).canTransitionToComplete();
    verify(reservation, times(1)).complete();
  }

  @Test
  @DisplayName("존재하지 않는 예약인 경우 예외가 발생한다")
  void shouldThrowException_WhenReservationNotFound() {
    // given
    val nonExistingReservationId = 999L;
    when(reservationRepository.findById(nonExistingReservationId)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(
        () -> interactor.completeReservation(nonExistingReservationId))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  @DisplayName("예약이 이미 완료된 경우 예외가 발생한다")
  void shouldThrowException_WhenReservationAlreadyCompleted() {
    // given
    val reservationId = 1L;
    val reservation = mock(Reservation.class);

    when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToComplete()).thenReturn(false);

    // when
    // then
    assertThatThrownBy(() -> interactor.completeReservation(reservationId))
        .isInstanceOf(IllegalStateException.class);
  }
}