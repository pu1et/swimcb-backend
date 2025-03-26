package com.project.swimcb.swimmingpool.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationStatus {
  RESERVATION_REQUEST("예약 신청"),
  RESERVATION_APPROVED("예약 승인"),
  PAYMENT_PENDING("입금 대기"),
  PAYMENT_COMPLETED("입금 완료"),
  REFUND_REQUEST("환불 신청"),
  REFUND_APPROVED("환불 승인"),
  REFUND_PENDING("환불 대기"),
  REFUND_COMPLETED("환불 완료");

  private final String description;
}
