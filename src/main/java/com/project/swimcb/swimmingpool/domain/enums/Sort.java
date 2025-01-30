package com.project.swimcb.swimmingpool.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Sort {
  DISTANCE_ASC("거리 가까운 순"),
  PRICE_ASC("낮은 가격순");

  private final String name;
}
