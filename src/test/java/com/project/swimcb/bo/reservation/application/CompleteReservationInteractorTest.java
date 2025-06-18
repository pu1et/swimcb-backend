package com.project.swimcb.bo.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.domain.CompleteReservationCommand;
import com.project.swimcb.db.entity.ReservationEntity;
import com.project.swimcb.db.repository.ReservationRepository;
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
class CompleteReservationInteractorTest {

  @InjectMocks
  private CompleteReservationInteractor interactor;

  @Mock
  private ReservationRepository reservationRepository;

  private CompleteReservationCommand command;

  @BeforeEach
  void setUp() {
    command = CompleteReservationCommand.builder()
        .reservationId(1L)
        .paymentMethod(CASH_ON_SITE)
        .build();
  }

  @Test
  @DisplayName("예약이 존재하는 경우 예약 완료를 성공적으로 처리한다")
  void shouldCompleteReservationSuccessfully() {
    // given
    val reservation = mock(ReservationEntity.class);

    when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToComplete()).thenReturn(true);

    // when
    interactor.completeReservation(command);

    // then
    verify(reservationRepository, only()).findById(command.reservationId());
    verify(reservation, times(1)).canTransitionToComplete();
    verify(reservation, times(1)).complete(command.paymentMethod());
  }

  @Test
  @DisplayName("존재하지 않는 예약인 경우 예외가 발생한다")
  void shouldThrowException_WhenReservationNotFound() {
    // given
    val nonExistingCommand = CompleteReservationCommand.builder()
        .reservationId(999L)
        .paymentMethod(CASH_ON_SITE)
        .build();
    when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(
        () -> interactor.completeReservation(nonExistingCommand))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  @DisplayName("예약이 이미 완료된 경우 예외가 발생한다")
  void shouldThrowException_WhenReservationAlreadyCompleted() {
    // given
    val reservation = mock(ReservationEntity.class);

    when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToComplete()).thenReturn(false);

    // when
    // then
    assertThatThrownBy(() -> interactor.completeReservation(command))
        .isInstanceOf(IllegalStateException.class);
  }
}
