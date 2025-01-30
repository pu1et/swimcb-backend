package com.project.swimcb.swimmingpool.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ClassType {
  GROUP_BEGINNER("단체강습(기초)"),
  GROUP_INTERMEDIATE("단체강습(초급)"),
  GROUP_ADVANCED("단체강습(중급)"),
  GROUP_EXPERT("단체강습(상급)"),
  GROUP_PROFESSIONAL("단체강습(연수)"),
  GROUP_MASTERS("단체강습(마스터즈)"),
  KIDS_SWIMMING("아동수영"),
  AQUA_AEROBICS("아쿠아로빅"),
  SPECIAL_CLASS("특별반"),
  PRIVATE_LESSON("레슨");

  private final String name;
}
