package com.project.swimcb.bo.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.assertArg;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.application.port.out.BoAutoCancelReservationsDsGateway;
import java.util.Collections;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoAutoCancelReservationsInteractorTest {

  @Mock
  private BoAutoCancelReservationsDsGateway gateway;

  private AutoCancelReservationsInteractor interactor;

  @BeforeEach
  void setUp() {
    interactor = new AutoCancelReservationsInteractor(gateway);
  }

  @Nested
  @DisplayName("수영장 기준 결제대기 24시간 초과 예약건 자동 취소")
  class CancelBySwimmingPoolId {

    @Nested
    @DisplayName("24시간 초과된 예약이 없는 경우")
    class WhenNoExpiredReservations {

      @Test
      @DisplayName("아무 작업도 수행하지 않는다")
      void shouldDoNothing() {
        // given
        val swimmingPoolId = 1L;
        when(gateway.findPaymentExpiredReservationsBySwimmingPoolId(swimmingPoolId)).thenReturn(
            List.of());

        // when
        interactor.cancelPaymentExpiredReservationsBySwimmingPoolId(swimmingPoolId);

        // then
        verify(gateway, only()).findPaymentExpiredReservationsBySwimmingPoolId(swimmingPoolId);
        verifyNoMoreInteractions(gateway);
      }

    }

    @Nested
    @DisplayName("24시간 초과된 예약이 있는 경우")
    class WhenExpiredReservationsExist {

      @Test
      @DisplayName("예약을 취소하고 대기 예약을 결제대기 상태로 변경한다")
      void shouldCancelAndAdvanceWaitingReservations() {
        // given
        val swimmingPoolId = 1L;
        val classId1 = 101L;
        val classId2 = 102L;

        val expiredReservations = List.of(
            new PaymentExpiredReservation(1L, classId1),
            new PaymentExpiredReservation(2L, classId1),
            new PaymentExpiredReservation(3L, classId2)
        );

        val pendingReservationIds = List.of(5L, 6L);

        when(gateway.findPaymentExpiredReservationsBySwimmingPoolId(swimmingPoolId))
            .thenReturn(expiredReservations);
        when(gateway.findReservationPendingReservations(any())).thenReturn(pendingReservationIds);

        // when
        interactor.cancelPaymentExpiredReservationsBySwimmingPoolId(swimmingPoolId);

        // then
        verify(gateway).findPaymentExpiredReservationsBySwimmingPoolId(swimmingPoolId);

        verify(gateway).cancelExpiredReservations(
            assertArg(i -> assertThat(i).containsExactlyInAnyOrder(1L, 2L, 3L)));

        verify(gateway).reduceSwimmingClassReservedCount(
            assertArg(i -> assertThat(i).containsExactlyInAnyOrder(classId1, classId2))
        );

        verify(gateway).findReservationPendingReservations(assertArg(i -> {
          assertThat(i).containsEntry(classId1, 2L);
          assertThat(i).containsEntry(classId2, 1L);
        }));

        verify(gateway).convertPendingReservationsToPaymentPending(pendingReservationIds);
      }

      @Test
      @DisplayName("대기 예약이 없으면 결제대기 상태로 변경하지 않는다")
      void shouldNotAdvanceWhenNoWaitingReservations() {
        // given
        val swimmingPoolId = 1L;
        val expiredReservations = List.of(new PaymentExpiredReservation(1L, 101L));

        when(gateway.findPaymentExpiredReservationsBySwimmingPoolId(swimmingPoolId))
            .thenReturn(expiredReservations);
        when(gateway.findReservationPendingReservations(any())).thenReturn(Collections.emptyList());

        // when
        interactor.cancelPaymentExpiredReservationsBySwimmingPoolId(swimmingPoolId);

        // then
        verify(gateway).findPaymentExpiredReservationsBySwimmingPoolId(swimmingPoolId);
        verify(gateway).cancelExpiredReservations(anyList());
        verify(gateway).reduceSwimmingClassReservedCount(anyList());
        verify(gateway).findReservationPendingReservations(any());
        verify(gateway, never()).convertPendingReservationsToPaymentPending(anyList());
      }

    }

  }

  @Nested
  @DisplayName("회원 기준 결제대기 24시간 초과 예약건 자동 취소")
  class CancelByMemberId {

    @Nested
    @DisplayName("24시간 초과된 예약이 없는 경우")
    class WhenNoExpiredReservations {

      @Test
      @DisplayName("아무 작업도 수행하지 않는다")
      void shouldDoNothing() {
        // given
        val memberId = 1L;
        when(gateway.findPaymentExpiredReservationsByMemberId(memberId)).thenReturn(List.of());

        // when
        interactor.cancelPaymentExpiredReservationsByMemberId(memberId);

        // then
        verify(gateway, only()).findPaymentExpiredReservationsByMemberId(memberId);
        verifyNoMoreInteractions(gateway);
      }

    }

    @Nested
    @DisplayName("24시간 초과된 예약이 있는 경우")
    class WhenExpiredReservationsExist {

      @Test
      @DisplayName("예약을 취소하고 대기 예약을 결제대기 상태로 변경한다")
      void shouldCancelAndAdvanceWaitingReservations() {
        // given
        val memberId = 1L;
        val classId1 = 101L;
        val classId2 = 102L;

        val expiredReservations = List.of(
            new PaymentExpiredReservation(1L, classId1),
            new PaymentExpiredReservation(2L, classId1),
            new PaymentExpiredReservation(3L, classId2)
        );

        val pendingReservationIds = List.of(5L, 6L);

        when(gateway.findPaymentExpiredReservationsByMemberId(memberId))
            .thenReturn(expiredReservations);
        when(gateway.findReservationPendingReservations(any())).thenReturn(pendingReservationIds);

        // when
        interactor.cancelPaymentExpiredReservationsByMemberId(memberId);

        // then
        verify(gateway).findPaymentExpiredReservationsByMemberId(memberId);

        verify(gateway).cancelExpiredReservations(
            assertArg(i -> assertThat(i).containsExactlyInAnyOrder(1L, 2L, 3L)));

        verify(gateway).reduceSwimmingClassReservedCount(
            assertArg(i -> assertThat(i).containsExactlyInAnyOrder(classId1, classId2))
        );

        verify(gateway).findReservationPendingReservations(assertArg(i -> {
          assertThat(i).containsEntry(classId1, 2L);
          assertThat(i).containsEntry(classId2, 1L);
        }));

        verify(gateway).convertPendingReservationsToPaymentPending(pendingReservationIds);
      }

      @Test
      @DisplayName("대기 예약이 없으면 결제대기 상태로 변경하지 않는다")
      void shouldNotAdvanceWhenNoWaitingReservations() {
        // given
        val memberId = 1L;
        val expiredReservations = List.of(
            new PaymentExpiredReservation(1L, 101L)
        );

        when(gateway.findPaymentExpiredReservationsByMemberId(memberId))
            .thenReturn(expiredReservations);
        when(gateway.findReservationPendingReservations(any())).thenReturn(Collections.emptyList());

        // when
        interactor.cancelPaymentExpiredReservationsByMemberId(memberId);

        // then
        verify(gateway).findPaymentExpiredReservationsByMemberId(memberId);
        verify(gateway).cancelExpiredReservations(anyList());
        verify(gateway).reduceSwimmingClassReservedCount(anyList());
        verify(gateway).findReservationPendingReservations(any());
        verify(gateway, never()).convertPendingReservationsToPaymentPending(anyList());
      }

    }

  }

}
