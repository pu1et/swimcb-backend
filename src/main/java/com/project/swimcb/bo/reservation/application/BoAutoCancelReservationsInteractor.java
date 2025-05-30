package com.project.swimcb.bo.reservation.application;

import com.project.swimcb.bo.reservation.application.port.in.BoAutoCancelReservationsUseCase;
import com.project.swimcb.bo.reservation.application.port.out.BoAutoCancelReservationsDsGateway;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class BoAutoCancelReservationsInteractor implements BoAutoCancelReservationsUseCase {

  private final BoAutoCancelReservationsDsGateway gateway;

  @Override
  public void cancelPaymentExpiredReservations(@NonNull Long swimmingPoolId) {

    val paymentExpiredReservations = gateway.findPaymentExpiredReservations(swimmingPoolId);
    if (paymentExpiredReservations.isEmpty()) {
      return;
    }

    // 결제대기가 24시간 초과된 예약 자동 취소 처리
    val reservationIds = paymentExpiredReservations.stream()
        .map(PaymentExpiredReservation::reservationId)
        .toList();
    gateway.cancelExpiredReservations(reservationIds);

    // 수영 클래스별 예약된 인원수 감소 처리
    val swimmingClassIds = paymentExpiredReservations.stream()
        .map(PaymentExpiredReservation::swimmingClassId)
        .distinct()
        .toList();
    gateway.reduceSwimmingClassReservedCount(swimmingClassIds);

    // 자동취소된 예약건 만큼 예약대기 건 조회
    val reservationsCountByClass = paymentExpiredReservations.stream()
        .collect(Collectors.groupingBy(
            PaymentExpiredReservation::swimmingClassId,
            Collectors.counting()
        ));
    val pendingReservations = gateway.findReservationPendingReservations(reservationsCountByClass);
    if (pendingReservations.isEmpty()) {
      return;
    }

    // 조회된 예약대기 건들 전부 결제대기로 전환
    gateway.convertPendingReservationsToPaymentPending(pendingReservations);
  }

}
