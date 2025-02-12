package com.project.swimcb.bo.reservation.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {

  BANK_TRANSFER("계좌이체"),
  CASH_ON_SITE("현장결제");

  private final String description;
}
