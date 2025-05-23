package com.project.swimcb.mypage.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.CancellationReason.USER_CANCELLED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;

import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.project.swimcb.mypage.reservation.application.port.in.CancelReservationUseCase;
import com.project.swimcb.swimmingpool.domain.Reservation;
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CancelReservationInteractor implements CancelReservationUseCase {

  private final ReservationRepository reservationRepository;
  private final BoCancelReservationDsGateway boCancelReservationDsGateway;

  @Override
  public void cancelReservation(@NonNull Long memberId, @NonNull Long reservationId) {
    val reservation = reservationRepository.findByIdAndMemberId(reservationId, memberId)
        .orElseThrow(() -> new NoSuchElementException("예약을 찾을 수 없습니다 : " + reservationId));

    if (!reservation.canTransitionToCancelByUser()) {
      throw new IllegalStateException("예약을 취소할 수 없습니다 : " + reservation);
    }
    reservation.cancel(USER_CANCELLED);

    val swimmingClassId = boCancelReservationDsGateway.findSwimmingClassByReservationId(
        reservationId);
    boCancelReservationDsGateway.updateSwimmingClassReservedCount(swimmingClassId, -1);

    updateWaitingStatusAfterReservation(reservation);
  }

  // 취소한 예약이 어떤 상태냐에 따라 처리가 다름
  // 1. 결제대기 상태 -> 가장 앞 순번인 예약대기를 결제대기로 변경
  // 2. 예약대기 상태 -> 아무것도 하지 않음
  private void updateWaitingStatusAfterReservation(@NonNull Reservation reservation) {
    if (reservation.getReservationStatus() == RESERVATION_PENDING) {
      return;
    }
    boCancelReservationDsGateway.findFirstWaitingReservationId(reservation.getId())
        .ifPresent(boCancelReservationDsGateway::updateReservationStatusToPaymentPending);
  }

}
