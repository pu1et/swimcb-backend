package com.project.swimcb.survey.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SurveyDissatisfactionReason {

  SWIMMING_POOL_INACCURATE("수영장 정보가 정확하지 않아요"),
  SWIMMING_POOL_HARD_TO_SEE("수영장 정보가 눈에 잘 안들어와요"),

  PROGRAM_INFO_INACCURATE("프로그램 정보가 정확하지 않아요"),
  PROGRAM_INFO_HARD_TO_SEE("프로그램 정보가 눈에 잘 안들어와요"),

  FONT_SIZE_INAPPROPRIATE("글씨 크기가 적당하지 않아요"),
  TOUCH_HARD("터치하기 어려워요"),
  OPERATION_COMPLEX("조작법이 까다로워요"),
  SIGNUP_COMPLEX("회원가입 및 로그인 과정이 복잡해요");

  private final String description;
}
