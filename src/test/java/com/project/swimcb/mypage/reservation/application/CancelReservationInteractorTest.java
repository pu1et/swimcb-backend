package com.project.swimcb.mypage.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.CancellationReason.USER_CANCELLED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.project.swimcb.db.entity.ReservationEntity;
import com.project.swimcb.db.entity.SwimmingClassEntity;
import com.project.swimcb.db.repository.ReservationRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

  private final Long MEMBER_ID = 1L;
  private final Long RESERVATION_ID = 2L;
  private final Long SWIMMING_CLASS_ID = 3L;

  @Nested
  @DisplayName("예약 취소 시")
  class CancelReservationTest {

    @Test
    @DisplayName("결제대기 상태인 경우 예약이 취소되고 대기 예약을 결제대기로 변경하고 예약수가 유지된다")
    void shouldCancelPaymentPendingReservationAndUpdateWaitingReservation() {
      // given
      val reservation = mock(ReservationEntity.class);
      val swimmingClass = mock(SwimmingClassEntity.class);

      when(reservationRepository.findByIdAndMemberId(RESERVATION_ID, MEMBER_ID))
          .thenReturn(Optional.of(reservation));
      when(reservation.canTransitionToCancelByUser()).thenReturn(true);
      when(reservation.getReservationStatus()).thenReturn(PAYMENT_PENDING);
      when(reservation.getSwimmingClass()).thenReturn(swimmingClass);
      when(swimmingClass.getId()).thenReturn(SWIMMING_CLASS_ID);

      val waitingReservationId = 4L;
      when(boCancelReservationDsGateway.findFirstWaitingReservationIdBySwimmingClassId(
          SWIMMING_CLASS_ID)).thenReturn(Optional.of(waitingReservationId));

      // when
      interactor.cancelReservation(MEMBER_ID, RESERVATION_ID);

      // then
      val inOrder = inOrder(reservationRepository, reservation, boCancelReservationDsGateway,
          reservation);

      inOrder.verify(reservation).canTransitionToCancelByUser();
      inOrder.verify(boCancelReservationDsGateway).updateSwimmingClassReservedCount(SWIMMING_CLASS_ID, -1);
      inOrder.verify(boCancelReservationDsGateway).findFirstWaitingReservationIdBySwimmingClassId(
          SWIMMING_CLASS_ID);
      inOrder.verify(boCancelReservationDsGateway).updateReservationStatusToPaymentPending(
          waitingReservationId);

      verify(reservation).cancel(USER_CANCELLED);
    }

    @Test
    @DisplayName("예약대기 상태인 경우 예약이 취소되고 대기 예약은 업데이트하지 않으며 예약수만 감소된다")
    void shouldCancelReservationPendingAndNotUpdateWaitingReservation() {
      // given
      val reservation = mock(ReservationEntity.class);
      val swimmingClass = mock(SwimmingClassEntity.class);

      when(reservationRepository.findByIdAndMemberId(RESERVATION_ID, MEMBER_ID))
          .thenReturn(Optional.of(reservation));
      when(reservation.canTransitionToCancelByUser()).thenReturn(true);
      when(reservation.getReservationStatus()).thenReturn(RESERVATION_PENDING);
      when(reservation.getSwimmingClass()).thenReturn(swimmingClass);
      when(swimmingClass.getId()).thenReturn(SWIMMING_CLASS_ID);

      // when
      interactor.cancelReservation(MEMBER_ID, RESERVATION_ID);

      // then
      val inOrder = inOrder(reservationRepository, reservation, boCancelReservationDsGateway, reservation);

      inOrder.verify(reservation).canTransitionToCancelByUser();
      inOrder.verify(boCancelReservationDsGateway).updateSwimmingClassReservedCount(SWIMMING_CLASS_ID, -1);
      inOrder.verify(reservation).cancel(USER_CANCELLED);

      verify(boCancelReservationDsGateway, never()).findFirstWaitingReservationIdBySwimmingClassId(
          anyLong());
      verify(boCancelReservationDsGateway, never()).updateReservationStatusToPaymentPending(
          anyLong());
    }

    @Test
    @DisplayName("결제대기 상태이지만 대기 예약이 없는 경우 예약만 취소되고 예약수만 감소된다")
    void shouldCancelPaymentPendingReservationWhenNoWaitingReservation() {
      // given
      val reservation = mock(ReservationEntity.class);
      val swimmingClass = mock(SwimmingClassEntity.class);

      when(reservationRepository.findByIdAndMemberId(RESERVATION_ID, MEMBER_ID))
          .thenReturn(Optional.of(reservation));
      when(reservation.canTransitionToCancelByUser()).thenReturn(true);
      when(reservation.getReservationStatus()).thenReturn(PAYMENT_PENDING);
      when(reservation.getSwimmingClass()).thenReturn(swimmingClass);
      when(swimmingClass.getId()).thenReturn(SWIMMING_CLASS_ID);

      when(boCancelReservationDsGateway.findFirstWaitingReservationIdBySwimmingClassId(
          SWIMMING_CLASS_ID)).thenReturn(Optional.empty());

      // when
      interactor.cancelReservation(MEMBER_ID, RESERVATION_ID);

      // then
      val inOrder = inOrder(reservationRepository, reservation, boCancelReservationDsGateway, reservation);

      inOrder.verify(reservation).canTransitionToCancelByUser();
      inOrder.verify(boCancelReservationDsGateway).updateSwimmingClassReservedCount(SWIMMING_CLASS_ID, -1);
      inOrder.verify(boCancelReservationDsGateway).findFirstWaitingReservationIdBySwimmingClassId(
          SWIMMING_CLASS_ID);
      inOrder.verify(reservation).cancel(USER_CANCELLED);

      verify(boCancelReservationDsGateway, never()).updateReservationStatusToPaymentPending(
          anyLong());
      // 예약수 감소만 1번 호출되어야 함
      verify(boCancelReservationDsGateway, times(1)).updateSwimmingClassReservedCount(anyLong(),
          anyInt());
    }

    @Test
    @DisplayName("예약이 존재하지 않으면 NoSuchElementException 예외를 발생시킨다")
    void shouldThrowNoSuchElementExceptionWhenReservationNotExists() {
      // given
      when(reservationRepository.findByIdAndMemberId(RESERVATION_ID, MEMBER_ID))
          .thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> interactor.cancelReservation(MEMBER_ID, RESERVATION_ID))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessageContaining("예약을 찾을 수 없습니다");

      verify(boCancelReservationDsGateway, never()).updateSwimmingClassReservedCount(anyLong(),
          anyInt());
      verify(boCancelReservationDsGateway, never()).findFirstWaitingReservationIdBySwimmingClassId(
          anyLong());
      verify(boCancelReservationDsGateway, never()).updateReservationStatusToPaymentPending(
          anyLong());
    }

    @Test
    @DisplayName("취소할 수 없는 상태면 IllegalStateException 예외를 발생시킨다")
    void shouldThrowIllegalStateExceptionWhenCannotCancel() {
      // given
      val reservation = mock(ReservationEntity.class);
      when(reservationRepository.findByIdAndMemberId(RESERVATION_ID, MEMBER_ID))
          .thenReturn(Optional.of(reservation));
      when(reservation.canTransitionToCancelByUser()).thenReturn(false);

      // when & then
      assertThatThrownBy(() -> interactor.cancelReservation(MEMBER_ID, RESERVATION_ID))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("예약을 취소할 수 없습니다");

      verify(reservation, never()).cancel(any());
      verify(boCancelReservationDsGateway, never()).updateSwimmingClassReservedCount(anyLong(),
          anyInt());
      verify(boCancelReservationDsGateway, never()).findFirstWaitingReservationIdBySwimmingClassId(
          anyLong());
      verify(boCancelReservationDsGateway, never()).updateReservationStatusToPaymentPending(
          anyLong());
    }

    @Test
    @DisplayName("파라미터가 null이면 NullPointerException 예외를 발생시킨다")
    void shouldThrowNullPointerExceptionWhenArgumentIsNull() {
      // when & then
      assertThatThrownBy(() -> interactor.cancelReservation(null, RESERVATION_ID))
          .isInstanceOf(NullPointerException.class);

      assertThatThrownBy(() -> interactor.cancelReservation(MEMBER_ID, null))
          .isInstanceOf(NullPointerException.class);

      verify(reservationRepository, never()).findByIdAndMemberId(anyLong(), anyLong());
      verify(boCancelReservationDsGateway, never()).updateSwimmingClassReservedCount(anyLong(),
          anyInt());
    }

  }

}
