package com.project.swimcb.swimmingpool.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentMethod {
  CASH_ON_SITE("현장결제"),
  BANK_TRANSFER("계좌이체");

  private final String description;
}
