package com.project.swimcb.swimmingpool.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CancellationReason {

  USER_CANCELLED("사용자취소"),
  PAYMENT_DEADLINE_EXPIRED("입금기한만료"),
  NO_PAYMENT_RECEIVED("입금미확인"),
  SWIMMING_CLASS_CANCELLED("폐강");

  private final String description;
}
