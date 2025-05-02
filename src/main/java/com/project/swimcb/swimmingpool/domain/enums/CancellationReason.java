package com.project.swimcb.swimmingpool.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CancellationReason {

  USER_CANCELLED("사용자 취소"),
  PAYMENT_DEADLINE_EXPIRED("입금기한만료");

  private final String description;
}
