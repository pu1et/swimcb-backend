package com.project.swimcb.swimmingpool.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SwimmingClassType {
  GROUP("단체강습"),
  KIDS_SWIMMING("아동수영"),
  AQUA_AEROBICS("아쿠아로빅"),
  SPECIAL_CLASS("특별반"),
  PRIVATE_LESSON("레슨");

  private final String name;
}
