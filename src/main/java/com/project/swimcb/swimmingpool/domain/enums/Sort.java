package com.project.swimcb.swimmingpool.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Sort {
  DISTANCE_ASC("거리 가까운 순"),
  PRICE_ASC("낮은 가격순"),
  PRICE_DESC("높은 가격순"),
  REGISTRATION_START_ASC("신청 시작일 빠른 순"),
  TIME_ASC("빠른 시간대 순"),
  TIME_DESC("늦은 시간대 순"),
  STUDENT_COUNT_DESC("많은 수강 인원 순"),
  STUDENT_COUNT_ASC("적은 수강 인원 순");

  private final String name;
}
