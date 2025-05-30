package com.project.swimcb.bo.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.CancellationReason.NO_PAYMENT_RECEIVED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;

import com.project.swimcb.bo.reservation.application.port.in.BoCancelReservationUseCase;
import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class BoCancelReservationInteractor implements BoCancelReservationUseCase {

  private final ReservationRepository reservationRepository;
  private final BoCancelReservationDsGateway boCancelReservationDsGateway;

  @Override
  public void cancelReservation(@NonNull Long reservationId) {
    val reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new NoSuchElementException("예약이 존재하지 않습니다 : " + reservationId));

    if (!reservation.canTransitionToCancelByAdmin()) {
      throw new IllegalStateException("입금 확인 중 상태만 취소할 수 있습니다 : " + reservation);
    }

    val swimmingClassId = reservation.getSwimmingClass().getId();

    boCancelReservationDsGateway.updateSwimmingClassReservedCount(swimmingClassId, -1);
    updateWaitingStatusAfterReservation(reservation.getReservationStatus(), swimmingClassId);

    reservation.cancel(NO_PAYMENT_RECEIVED);
  }

  // 취소한 예약이 어떤 상태냐에 따라 처리가 다름
  // 1. 결제대기 상태 -> 가장 앞 순번인 예약대기를 결제대기로 변경
  // 2. 예약대기 상태 -> 아무것도 하지 않음
  private void updateWaitingStatusAfterReservation(
      @NonNull ReservationStatus reservationStatus,
      @NonNull Long swimmingClassId
  ) {
    if (reservationStatus == RESERVATION_PENDING) {
      return;
    }
    boCancelReservationDsGateway.findFirstWaitingReservationIdBySwimmingClassId(swimmingClassId)
        .ifPresent(boCancelReservationDsGateway::updateReservationStatusToPaymentPending);
  }

}
