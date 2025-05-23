package com.project.swimcb.mypage.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.CancellationReason.USER_CANCELLED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.project.swimcb.swimmingpool.domain.Reservation;
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
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
    @DisplayName("결제대기 상태인 경우 예약이 취소되고 대기 예약을 결제대기로 변경한다")
    void shouldCancelPaymentPendingReservationAndUpdateWaitingReservation() {
      // given
      val reservation = mock(Reservation.class);
      when(reservationRepository.findByIdAndMemberId(RESERVATION_ID, MEMBER_ID))
          .thenReturn(Optional.of(reservation));
      when(reservation.getId()).thenReturn(RESERVATION_ID);
      when(reservation.canTransitionToCancelByUser()).thenReturn(true);
      when(reservation.getReservationStatus()).thenReturn(PAYMENT_PENDING);

      when(boCancelReservationDsGateway.findSwimmingClassByReservationId(RESERVATION_ID))
          .thenReturn(SWIMMING_CLASS_ID);

      val waitingReservationId = 4L;
      when(boCancelReservationDsGateway.findFirstWaitingReservationId(RESERVATION_ID))
          .thenReturn(Optional.of(waitingReservationId));

      // when
      interactor.cancelReservation(MEMBER_ID, RESERVATION_ID);

      // then
      verify(reservation).cancel(USER_CANCELLED);
      verify(boCancelReservationDsGateway, times(1)).updateSwimmingClassReservedCount(SWIMMING_CLASS_ID, -1);
      verify(boCancelReservationDsGateway, times(1)).updateReservationStatusToPaymentPending(waitingReservationId);
      verify(boCancelReservationDsGateway, times(1))
          .findFirstWaitingReservationId(RESERVATION_ID);
      verify(boCancelReservationDsGateway).updateReservationStatusToPaymentPending(
          waitingReservationId);
    }

    @Test
    @DisplayName("예약대기 상태인 경우 예약이 취소되고 대기 예약은 업데이트하지 않는다")
    void shouldCancelReservationPendingAndNotUpdateWaitingReservation() {
      // given
      val reservation = mock(Reservation.class);
      when(reservationRepository.findByIdAndMemberId(RESERVATION_ID, MEMBER_ID))
          .thenReturn(Optional.of(reservation));
      when(reservation.canTransitionToCancelByUser()).thenReturn(true);
      when(reservation.getReservationStatus()).thenReturn(RESERVATION_PENDING);

      when(boCancelReservationDsGateway.findSwimmingClassByReservationId(RESERVATION_ID))
          .thenReturn(SWIMMING_CLASS_ID);

      // when
      interactor.cancelReservation(MEMBER_ID, RESERVATION_ID);

      // then
      verify(reservation).cancel(USER_CANCELLED);
      verify(boCancelReservationDsGateway, times(1)).updateSwimmingClassReservedCount(SWIMMING_CLASS_ID, -1);
      verify(boCancelReservationDsGateway, never()).findFirstWaitingReservationId(anyLong());
      verify(boCancelReservationDsGateway, never()).updateReservationStatusToPaymentPending(anyLong());
    }

    @Test
    @DisplayName("결제대기 상태이지만 대기 예약이 없는 경우 예약만 취소된다")
    void shouldCancelPaymentPendingReservationWhenNoWaitingReservation() {
      // given
      val reservation = mock(Reservation.class);
      when(reservationRepository.findByIdAndMemberId(RESERVATION_ID, MEMBER_ID))
          .thenReturn(Optional.of(reservation));
      when(reservation.getId()).thenReturn(RESERVATION_ID);
      when(reservation.canTransitionToCancelByUser()).thenReturn(true);
      when(reservation.getReservationStatus()).thenReturn(PAYMENT_PENDING);

      when(boCancelReservationDsGateway.findSwimmingClassByReservationId(RESERVATION_ID))
          .thenReturn(SWIMMING_CLASS_ID);

      when(boCancelReservationDsGateway.findFirstWaitingReservationId(RESERVATION_ID))
          .thenReturn(Optional.empty());

      // when
      interactor.cancelReservation(MEMBER_ID, RESERVATION_ID);

      // then
      verify(reservation).cancel(USER_CANCELLED);
      verify(boCancelReservationDsGateway).updateSwimmingClassReservedCount(SWIMMING_CLASS_ID, -1);
      verify(boCancelReservationDsGateway, never()).updateReservationStatusToPaymentPending(anyLong());
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
    }

    @Test
    @DisplayName("취소할 수 없는 상태면 IllegalStateException 예외를 발생시킨다")
    void shouldThrowIllegalStateExceptionWhenCannotCancel() {
      // given
      val reservation = mock(Reservation.class);
      when(reservationRepository.findByIdAndMemberId(RESERVATION_ID, MEMBER_ID))
          .thenReturn(Optional.of(reservation));
      when(reservation.canTransitionToCancelByUser()).thenReturn(false);

      // when
      // then
      assertThatThrownBy(() -> interactor.cancelReservation(MEMBER_ID, RESERVATION_ID))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("예약을 취소할 수 없습니다");
    }

    @Test
    @DisplayName("파라미터가 null이면 NullPointerException 예외를 발생시킨다")
    void shouldThrowNullPointerExceptionWhenArgumentIsNull() {
      // when
      // then
      assertThatThrownBy(() -> interactor.cancelReservation(null, RESERVATION_ID))
          .isInstanceOf(NullPointerException.class);

      assertThatThrownBy(() -> interactor.cancelReservation(MEMBER_ID, null))
          .isInstanceOf(NullPointerException.class);
    }
  }
}
