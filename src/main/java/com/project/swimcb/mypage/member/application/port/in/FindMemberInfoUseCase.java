package com.project.swimcb.mypage.member.application.port.in;

import com.project.swimcb.mypage.member.domain.MemberInfo;
import lombok.NonNull;

public interface FindMemberInfoUseCase {

  MemberInfo findMemberInfo(@NonNull Long memberId);

}
