package com.project.swimcb.mypage.member.application;

import com.project.swimcb.mypage.member.application.port.in.WithdrawUseCase;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class WithdrawInteractor implements WithdrawUseCase {

  @Override
  public void withdraw(@NonNull Long memberId) {
    // TODO. 기획 정해지면 작업 예정
  }

}
