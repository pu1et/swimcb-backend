package com.project.swimcb.bo.reservation.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.domain.BoRefundReservationCommand;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
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
class BoRefundReservationInteractorTest {

  @InjectMocks
  private BoRefundReservationInteractor interactor;

  @Mock
  private ReservationRepository repository;

  private BoRefundReservationCommand command;

  @BeforeEach
  void setUp() {
    command = BoRefundReservationCommand.builder()
        .reservationId(1L)
        .bankName("DUMMY_BANK_NAME")
        .accountNo("DUMMY_ACCOUNT_NO")
        .accountHolder("DUMMY_ACCOUNT_HOLDER")
        .amount(10000)
        .build();
  }

  @Test
  @DisplayName("환불로 변경이 가능한 예약이면, 환불 처리한다")
  void shouldRefundReservation_WhenCanTransitionToRefund() {
    // given
    val reservation = mock(Reservation.class);

    when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToRefund()).thenReturn(true);

    // when
    interactor.refundReservation(command);

    // then
    verify(repository, only()).findById(command.reservationId());
    verify(reservation, times(1)).canTransitionToRefund();
    verify(reservation, times(1)).refund(
        command.bankName(),
        new AccountNo(command.accountNo()),
        command.accountHolder(),
        command.amount()
    );
  }

  @Test
  @DisplayName("존재하지 않는 예약인 경우 예외가 발생한다")
  void shouldThrowException_WhenReservationNotFound() {
    // given
    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(
        () -> interactor.refundReservation(command))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  @DisplayName("환불로 변경이 불가능한 예약이면, 예외가 발생한다")
  void shouldThrowException_WhenCannotTransitionToCancel() {
    // given
    val reservation = mock(Reservation.class);

    when(repository.findById(anyLong())).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToRefund()).thenReturn(false);

    // when
    // then
    assertThatThrownBy(() -> interactor.refundReservation(command))
        .isInstanceOf(IllegalStateException.class);
  }
}