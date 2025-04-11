package com.project.swimcb.swimmingpool.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
  PAYMENT_PENDING("결제 대기"),
  PAYMENT_COMPLETED("결제 완료"),
  RESERVATION_PENDING("예약 대기"),
  RESERVATION_CANCELLED("예약 취소"),
  REFUND_COMPLETED("환불 완료");

  private final String description;
}
