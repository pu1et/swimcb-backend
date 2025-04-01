package com.project.swimcb.swimmingpool.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SwimmingClassAvailabilityStatus {

  NOT_RESERVABLE("예약 불가"),
  WAITING_RESERVABLE("대기 가능"),
  RESERVABLE("예약 가능");

  private final String description;

  public static SwimmingClassAvailabilityStatus calculateStatus(int reservationLimitCount,
      int reservedCount) {

    int reservableCount = reservationLimitCount - reservedCount;

    if (reservableCount > 0) {
      return RESERVABLE;
    }

    if (reservableCount > -4) {
      return WAITING_RESERVABLE;
    }

    return NOT_RESERVABLE;
  }
}
