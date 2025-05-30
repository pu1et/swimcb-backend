package com.project.swimcb.bo.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.assertArg;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.reservation.application.port.out.BoAutoCancelReservationsDsGateway;
import java.util.Collections;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoAutoCancelReservationsInteractorTest {

  @Mock
  private BoAutoCancelReservationsDsGateway gateway;

  private BoAutoCancelReservationsInteractor interactor;

  @BeforeEach
  void setUp() {
    interactor = new BoAutoCancelReservationsInteractor(gateway);
  }

  @Test
  @DisplayName("결제대기 시간이 24시간이 초과된 예약이 없을 경우 아무 작업도 수행하지 않아야 한다")
  void shouldDoNothingWhenNoExpiredReservations() {
    // given
    val swimmingPoolId = 1L;
    when(gateway.findPaymentExpiredReservations(swimmingPoolId)).thenReturn(List.of());

    // when
    interactor.cancelPaymentExpiredReservations(swimmingPoolId);

    // then
    verify(gateway, only()).findPaymentExpiredReservations(swimmingPoolId);
    verifyNoMoreInteractions(gateway);
  }

  @Test
  @DisplayName("결제대기 시간이 24시간이 초과된 예약을 취소하고, 취소된 건 수만큼 대기예약을 앞당겨야한다")
  void shouldCancelExpiredReservationsAndAdvanceWaitingReservations() {
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

    when(gateway.findPaymentExpiredReservations(swimmingPoolId)).thenReturn(expiredReservations);
    when(gateway.findReservationPendingReservations(any())).thenReturn(pendingReservationIds);

    // when
    interactor.cancelPaymentExpiredReservations(swimmingPoolId);

    // then
    verify(gateway, times(1)).findPaymentExpiredReservations(swimmingPoolId);

    verify(gateway, times(1)).cancelExpiredReservations(assertArg(i -> {
      assertThat(i).containsExactlyInAnyOrder(1L, 2L, 3L);
    }));

    verify(gateway, times(1)).reduceSwimmingClassReservedCount(
        assertArg(i -> {
          assertThat(i).containsExactlyInAnyOrder(classId1, classId2);
        })
    );

    verify(gateway, times(1)).findReservationPendingReservations(assertArg(i -> {
      assertThat(i).containsEntry(classId1, 2L);
      assertThat(i).containsEntry(classId2, 1L);
    }));

    verify(gateway, times(1)).convertPendingReservationsToPaymentPending(pendingReservationIds);
  }

  @Test
  @DisplayName("대기중인 예약이 없을 경우 결제대기 상태로 전환하지 않아야 한다")
  void shouldNotConvertPendingReservationsWhenNoneFound() {
    // given
    val swimmingPoolId = 1L;
    val expiredReservations = List.of(
        new PaymentExpiredReservation(1L, 101L)
    );

    when(gateway.findPaymentExpiredReservations(swimmingPoolId)).thenReturn(expiredReservations);
    when(gateway.findReservationPendingReservations(any())).thenReturn(Collections.emptyList());

    // when
    interactor.cancelPaymentExpiredReservations(swimmingPoolId);

    // then
    verify(gateway, times(1)).findPaymentExpiredReservations(swimmingPoolId);
    verify(gateway, times(1)).cancelExpiredReservations(anyList());
    verify(gateway, times(1)).reduceSwimmingClassReservedCount(anyList());
    verify(gateway, times(1)).findReservationPendingReservations(any());
    verify(gateway, never()).convertPendingReservationsToPaymentPending(anyList());
  }
}
