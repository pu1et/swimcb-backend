package com.project.swimcb.bo.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.project.swimcb.bo.reservation.domain.BoRefundReservationCommand;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClass;
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

  @Mock
  private BoCancelReservationDsGateway boCancelReservationDsGateway;

  private BoRefundReservationCommand command;
  private final Long RESERVATION_ID = 1L;
  private final Long SWIMMING_CLASS_ID = 10L;

  @BeforeEach
  void setUp() {
    command = BoRefundReservationCommand.builder()
        .reservationId(RESERVATION_ID)
        .bankName("DUMMY_BANK_NAME")
        .accountNo("DUMMY_ACCOUNT_NO")
        .accountHolder("DUMMY_ACCOUNT_HOLDER")
        .amount(10000)
        .build();
  }

  @Test
  @DisplayName("환불로 변경이 가능하고 결제대기 상태의 예약이면, 환불 처리하고 대기 예약을 결제대기로 변경한다")
  void shouldRefundReservationAndUpdateWaitingReservation_WhenCanTransitionToRefund() {
    // given
    val reservation = mock(Reservation.class);
    val swimmingClass = mock(SwimmingClass.class);

    when(repository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToRefund()).thenReturn(true);
    when(reservation.getReservationStatus()).thenReturn(PAYMENT_PENDING);
    when(reservation.getSwimmingClass()).thenReturn(swimmingClass);
    when(swimmingClass.getId()).thenReturn(SWIMMING_CLASS_ID);

    val waitingReservationId = 5L;
    when(boCancelReservationDsGateway.findFirstWaitingReservationIdBySwimmingClassId(
        SWIMMING_CLASS_ID)).thenReturn(Optional.of(waitingReservationId));

    // when
    interactor.refundReservation(command);

    // then
    val inOrder = inOrder(repository, reservation, boCancelReservationDsGateway, reservation);

    inOrder.verify(reservation).canTransitionToRefund();
    inOrder.verify(boCancelReservationDsGateway)
        .updateSwimmingClassReservedCount(SWIMMING_CLASS_ID, -1);
    inOrder.verify(boCancelReservationDsGateway).findFirstWaitingReservationIdBySwimmingClassId(
        SWIMMING_CLASS_ID);
    inOrder.verify(boCancelReservationDsGateway).updateReservationStatusToPaymentPending(
        waitingReservationId);
    inOrder.verify(reservation).refund(
        command.bankName(),
        new AccountNo(command.accountNo()),
        command.accountHolder(),
        command.amount()
    );
  }

  @Test
  @DisplayName("환불로 변경이 가능하고 예약대기 상태의 예약이면, 환불 처리하고 대기 예약은 업데이트하지 않는다")
  void shouldRefundReservationWithoutUpdatingWaitingList_WhenReservationPending() {
    // given
    val reservation = mock(Reservation.class);
    val swimmingClass = mock(SwimmingClass.class);

    when(repository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToRefund()).thenReturn(true);
    when(reservation.getReservationStatus()).thenReturn(RESERVATION_PENDING);
    when(reservation.getSwimmingClass()).thenReturn(swimmingClass);
    when(swimmingClass.getId()).thenReturn(SWIMMING_CLASS_ID);

    // when
    interactor.refundReservation(command);

    // then
    val inOrder = inOrder(repository, reservation, boCancelReservationDsGateway, reservation);

    inOrder.verify(reservation).canTransitionToRefund();
    inOrder.verify(boCancelReservationDsGateway)
        .updateSwimmingClassReservedCount(SWIMMING_CLASS_ID, -1);
    inOrder.verify(reservation).refund(
        command.bankName(),
        new AccountNo(command.accountNo()),
        command.accountHolder(),
        command.amount()
    );

    verify(boCancelReservationDsGateway, never())
        .findFirstWaitingReservationIdBySwimmingClassId(anyLong());
    verify(boCancelReservationDsGateway, never()).updateReservationStatusToPaymentPending(
        anyLong());
  }

  @Test
  @DisplayName("환불 가능하고 결제대기 상태이지만 대기 예약이 없는 경우 환불만 처리한다")
  void shouldRefundReservationWithoutUpdatingWaitingList_WhenNoWaitingReservations() {
    // given
    val reservation = mock(Reservation.class);
    val swimmingClass = mock(SwimmingClass.class);

    when(repository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToRefund()).thenReturn(true);
    when(reservation.getReservationStatus()).thenReturn(PAYMENT_PENDING);
    when(reservation.getSwimmingClass()).thenReturn(swimmingClass);
    when(swimmingClass.getId()).thenReturn(SWIMMING_CLASS_ID);

    when(boCancelReservationDsGateway.findFirstWaitingReservationIdBySwimmingClassId(
        SWIMMING_CLASS_ID)).thenReturn(Optional.empty());

    // when
    interactor.refundReservation(command);

    // then
    val inOrder = inOrder(repository, reservation, boCancelReservationDsGateway, reservation);

    inOrder.verify(reservation).canTransitionToRefund();
    inOrder.verify(boCancelReservationDsGateway)
        .updateSwimmingClassReservedCount(SWIMMING_CLASS_ID, -1);
    inOrder.verify(boCancelReservationDsGateway).findFirstWaitingReservationIdBySwimmingClassId(
        SWIMMING_CLASS_ID);
    inOrder.verify(reservation).refund(
        command.bankName(),
        new AccountNo(command.accountNo()),
        command.accountHolder(),
        command.amount()
    );

    verify(boCancelReservationDsGateway, never()).updateReservationStatusToPaymentPending(
        anyLong());
  }

  @Test
  @DisplayName("존재하지 않는 예약인 경우 예외가 발생한다")
  void shouldThrowException_WhenReservationNotFound() {
    // given
    when(repository.findById(RESERVATION_ID)).thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> interactor.refundReservation(command))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("예약이 존재하지 않습니다");
  }

  @Test
  @DisplayName("환불로 변경이 불가능한 예약이면, 예외가 발생한다")
  void shouldThrowException_WhenCannotTransitionToRefund() {
    // given
    val reservation = mock(Reservation.class);

    when(repository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToRefund()).thenReturn(false);

    // when
    // then
    assertThatThrownBy(() -> interactor.refundReservation(command))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("결제완료|취소완료 상태에서만 환불이 가능합니다");
  }

  @Test
  @DisplayName("파라미터가 null이면 NullPointerException 예외를 발생시킨다")
  void shouldThrowNullPointerExceptionWhenArgumentIsNull() {
    // when
    // then
    assertThatThrownBy(() -> interactor.refundReservation(null))
        .isInstanceOf(NullPointerException.class);
  }

}
