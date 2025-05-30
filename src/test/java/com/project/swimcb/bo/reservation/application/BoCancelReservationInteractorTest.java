package com.project.swimcb.bo.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.CancellationReason.NO_PAYMENT_RECEIVED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClass;
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
class BoCancelReservationInteractorTest {

  @InjectMocks
  private BoCancelReservationInteractor interactor;

  @Mock
  private ReservationRepository repository;

  @Mock
  private BoCancelReservationDsGateway boCancelReservationDsGateway;

  private final Long RESERVATION_ID = 1L;
  private final Long SWIMMING_CLASS_ID = 10L;

  @Test
  @DisplayName("결제대기 상태 예약이면 취소 처리 후 대기 예약을 결제대기로 변경하고 예약수를 증가시킨다")
  void shouldCancelPaymentPendingReservationAndUpdateWaitingReservation() {
    // given
    val reservation = mock(Reservation.class);
    val swimmingClass = mock(SwimmingClass.class);

    when(repository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToCancelByAdmin()).thenReturn(true);
    when(reservation.getReservationStatus()).thenReturn(PAYMENT_PENDING);
    when(reservation.getSwimmingClass()).thenReturn(swimmingClass);
    when(swimmingClass.getId()).thenReturn(SWIMMING_CLASS_ID);

    val waitingReservationId = 5L;
    when(boCancelReservationDsGateway.findFirstWaitingReservationIdBySwimmingClassId(
        SWIMMING_CLASS_ID)).thenReturn(Optional.of(waitingReservationId));

    // when
    interactor.cancelReservation(RESERVATION_ID);

    // then
    val inOrder = inOrder(repository, reservation, boCancelReservationDsGateway, reservation);

    inOrder.verify(reservation).canTransitionToCancelByAdmin();
    inOrder.verify(boCancelReservationDsGateway, times(1)).updateSwimmingClassReservedCount(
        SWIMMING_CLASS_ID, -1);
    inOrder.verify(boCancelReservationDsGateway, times(1)).findFirstWaitingReservationIdBySwimmingClassId(
        SWIMMING_CLASS_ID);
    inOrder.verify(boCancelReservationDsGateway, times(1)).updateReservationStatusToPaymentPending(
        waitingReservationId);
    inOrder.verify(reservation).cancel(NO_PAYMENT_RECEIVED);
  }

  @Test
  @DisplayName("예약대기 상태인 경우 예약이 취소되고 대기 예약은 업데이트하지 않는다")
  void shouldCancelReservationPendingAndNotUpdateWaitingReservation() {
    // given
    val reservation = mock(Reservation.class);
    val swimmingClass = mock(SwimmingClass.class);

    when(repository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToCancelByAdmin()).thenReturn(true);
    when(reservation.getReservationStatus()).thenReturn(RESERVATION_PENDING);
    when(reservation.getSwimmingClass()).thenReturn(swimmingClass);
    when(swimmingClass.getId()).thenReturn(SWIMMING_CLASS_ID);

    // when
    interactor.cancelReservation(RESERVATION_ID);

    // then
    val inOrder = inOrder(repository, reservation, boCancelReservationDsGateway, reservation);

    inOrder.verify(reservation).canTransitionToCancelByAdmin();
    inOrder.verify(boCancelReservationDsGateway, times(1)).updateSwimmingClassReservedCount(
        SWIMMING_CLASS_ID, -1);
    inOrder.verify(boCancelReservationDsGateway, never()).findFirstWaitingReservationIdBySwimmingClassId(
        anyLong());
    inOrder.verify(reservation).cancel(NO_PAYMENT_RECEIVED);

    verify(boCancelReservationDsGateway, never()).updateReservationStatusToPaymentPending(
        anyLong());
  }

  @Test
  @DisplayName("결제대기 상태이지만 대기 예약이 없는 경우 예약만 취소되고 예약수만 감소된다")
  void shouldCancelPaymentPendingReservationWhenNoWaitingReservation() {
    // given
    val reservation = mock(Reservation.class);
    val swimmingClass = mock(SwimmingClass.class);

    when(repository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToCancelByAdmin()).thenReturn(true);
    when(reservation.getReservationStatus()).thenReturn(PAYMENT_PENDING);
    when(reservation.getSwimmingClass()).thenReturn(swimmingClass);
    when(swimmingClass.getId()).thenReturn(SWIMMING_CLASS_ID);

    when(boCancelReservationDsGateway.findFirstWaitingReservationIdBySwimmingClassId(
        SWIMMING_CLASS_ID)).thenReturn(Optional.empty());

    // when
    interactor.cancelReservation(RESERVATION_ID);

    // then
    val inOrder = inOrder(repository, reservation, boCancelReservationDsGateway, reservation);

    inOrder.verify(reservation).canTransitionToCancelByAdmin();
    inOrder.verify(boCancelReservationDsGateway, times(1)).updateSwimmingClassReservedCount(
        SWIMMING_CLASS_ID, -1);
    inOrder.verify(reservation).cancel(NO_PAYMENT_RECEIVED);

    verify(boCancelReservationDsGateway, never()).updateReservationStatusToPaymentPending(
        anyLong());
  }

  @Test
  @DisplayName("존재하지 않는 예약인 경우 예외가 발생한다")
  void shouldThrowException_WhenReservationNotFound() {
    // given
    when(repository.findById(RESERVATION_ID)).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> interactor.cancelReservation(RESERVATION_ID))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("예약이 존재하지 않습니다");
  }

  @Test
  @DisplayName("취소로 변경이 불가능한 예약이면, 예외가 발생한다")
  void shouldThrowException_WhenCannotTransitionToCancel() {
    // given
    val reservation = mock(Reservation.class);
    when(repository.findById(RESERVATION_ID)).thenReturn(Optional.of(reservation));
    when(reservation.canTransitionToCancelByAdmin()).thenReturn(false);

    // when & then
    assertThatThrownBy(() -> interactor.cancelReservation(RESERVATION_ID))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("입금 확인 중 상태만 취소할 수 있습니다");
  }

  @Test
  @DisplayName("파라미터가 null이면 NullPointerException 예외를 발생시킨다")
  void shouldThrowNullPointerExceptionWhenArgumentIsNull() {
    // when & then
    assertThatThrownBy(() -> interactor.cancelReservation(null))
        .isInstanceOf(NullPointerException.class);
  }

}
