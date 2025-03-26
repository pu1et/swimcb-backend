package com.project.swimcb.swimmingpool.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupFixedClassSubTypeName {
  BASIC("기초"),
  BEGINNER("초급"),
  INTERMEDIATE("중급"),
  ADVANCED("고급"),
  TRAINING("연수"),
  MASTERS("마스터스");

  private final String description;
}
