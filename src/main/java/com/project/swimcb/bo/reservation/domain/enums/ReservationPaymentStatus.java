package com.project.swimcb.bo.reservation.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationPaymentStatus {

  RESERVATION_REQUESTED("예약 요청"),
  RESERVATION_CANCELLED("예약 취소"),
  PAYMENT_COMPLETED("결제 완료"),
  PAYMENT_CANCELLED("결제 취소");

  private final String description;
}
