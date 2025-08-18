package com.project.swimcb.swimmingpool.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupFixedClassSubTypeName {
  BASIC("기초"),
  BEGINNER("초급"),
  INTERMEDIATE("중급"),
  BEGINNER_INTERMEDIATE("초중급"),
  INTERMEDIATE_ADVANCED("중상급"),
  ADVANCED("상급"),
  TRAINING("연수"),
  MASTERS("마스터즈"),
  COMPREHENSIVE("종합");

  private final String description;
}
