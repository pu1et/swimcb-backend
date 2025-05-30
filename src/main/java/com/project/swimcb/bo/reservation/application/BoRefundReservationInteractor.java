package com.project.swimcb.bo.reservation.application;

import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;

import com.project.swimcb.bo.reservation.application.port.in.BoRefundReservationUseCase;
import com.project.swimcb.bo.reservation.application.port.out.BoCancelReservationDsGateway;
import com.project.swimcb.bo.reservation.domain.BoRefundReservationCommand;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
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
class BoRefundReservationInteractor implements BoRefundReservationUseCase {

  private final ReservationRepository reservationRepository;
  private final BoCancelReservationDsGateway boCancelReservationDsGateway;

  @Override
  public void refundReservation(@NonNull BoRefundReservationCommand command) {
    val reservation = reservationRepository.findById(command.reservationId())
        .orElseThrow(
            () -> new NoSuchElementException("예약이 존재하지 않습니다 : " + command.reservationId()));

    if (!reservation.canTransitionToRefund()) {
      throw new IllegalStateException("결제완료|취소완료 상태에서만 환불이 가능합니다 : " + command.reservationId());
    }

    reservation.refund(
        command.bankName(),
        new AccountNo(command.accountNo()),
        command.accountHolder(),
        command.amount()
    );
    val swimmingClassId = reservation.getSwimmingClass().getId();

    boCancelReservationDsGateway.updateSwimmingClassReservedCount(swimmingClassId, -1);

    updateWaitingStatusAfterReservation(reservation.getReservationStatus(), swimmingClassId);
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
        .ifPresent(i -> {
          boCancelReservationDsGateway.updateReservationStatusToPaymentPending(i);
          boCancelReservationDsGateway.updateSwimmingClassReservedCount(swimmingClassId, 1);
        });
  }

}
