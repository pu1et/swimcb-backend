package com.project.swimcb.mypage.member.application.port.in;

import lombok.NonNull;

public interface WithdrawUseCase {

  void withdraw(@NonNull Long memberId);

}
