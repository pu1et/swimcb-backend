package com.project.swimcb.swimmingpool.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
  PAYMENT_PENDING("결제대기"),
  PAYMENT_COMPLETED("결제완료"),
  RESERVATION_PENDING("예약대기"),
  PAYMENT_VERIFICATION("입금확인중"),
  RESERVATION_CANCELLED("취소완료"),
  REFUND_COMPLETED("환불완료");

  private final String description;
}
