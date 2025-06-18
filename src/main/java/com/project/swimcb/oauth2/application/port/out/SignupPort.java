package com.project.swimcb.oauth2.application.port.out;

import com.project.swimcb.oauth2.domain.SignupRequest;
import lombok.NonNull;

public interface SignupPort {

  /**
   * 회원가입을 처리하는 메소드입니다.
   *
   * @param signupRequest 회원가입 요청 정보
   */
  void signup(@NonNull SignupRequest signupRequest);
}
