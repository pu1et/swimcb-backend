package com.project.swimcb.bo.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.CancellationReason.NO_PAYMENT_RECEIVED;
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
class BoCancelReservationInteractorTest {

  @InjectMocks
  private BoCancelReservationInteractor interactor;

  @Mock
  private ReservationRepository repository;

  @Mock
  private BoCancelReservationDsGateway boCancelReservationDsGateway;

  private Long reservationId;

  @BeforeEach
  void setUp() {
    reservationId = 1L;
  }

  @Test
  @DisplayName("취소로 변경이 가능한 예약이면, 입금미확인 사유로 취소 처리한다")
  void shouldCancelReservation_WhenCanTransitionToCancel() {
    // given
    val reservation = mock(Reservation.class);
    val swimmingClassId = 1L;

    when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToCancelByAdmin()).thenReturn(true);
    when(boCancelReservationDsGateway.findSwimmingClassByReservationId(reservationId))
        .thenReturn(swimmingClassId);

    // when
    interactor.cancelReservation(reservationId);

    // then
    verify(repository, only()).findById(reservationId);
    verify(reservation, times(1)).canTransitionToCancelByAdmin();
    verify(reservation, times(1)).cancel(NO_PAYMENT_RECEIVED);
    verify(boCancelReservationDsGateway, times(1)).findSwimmingClassByReservationId(reservationId);
    verify(boCancelReservationDsGateway, times(1))
        .updateSwimmingClassReservedCount(swimmingClassId, -1);
  }

  @Test
  @DisplayName("존재하지 않는 예약인 경우 예외가 발생한다")
  void shouldThrowException_WhenReservationNotFound() {
    // given
    val nonExistentReservationId = 999L;
    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(
        () -> interactor.cancelReservation(nonExistentReservationId))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  @DisplayName("취소로 변경이 불가능한 예약이면, 예외가 발생한다")
  void shouldThrowException_WhenCannotTransitionToCancel() {
    // given
    val reservation = mock(Reservation.class);

    when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToCancelByAdmin()).thenReturn(false);

    // when
    // then
    assertThatThrownBy(() -> interactor.cancelReservation(reservationId))
        .isInstanceOf(IllegalStateException.class);
  }
}
