package com.project.swimcb.mypage.member.application;

import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.mypage.member.application.port.in.FindMemberInfoUseCase;
import com.project.swimcb.mypage.member.domain.MemberInfo;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindMemberInfoInteractor implements FindMemberInfoUseCase {

  private final MemberRepository repository;

  @Override
  public MemberInfo findMemberInfo(@NonNull Long memberId) {
    val member = repository.findById(memberId)
        .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다 : " + memberId));

    return MemberInfo.builder()
        .name(member.getName())
        .phone(member.getPhone())
        .email(member.getEmail())
        .build();
  }

}
