package com.project.swimcb.mypage.member.application;

import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.mypage.member.application.port.in.WithdrawUseCase;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class WithdrawInteractor implements WithdrawUseCase {

  private final MemberRepository memberRepository;

  @Override
  public void withdraw(@NonNull Long memberId) {
    val member = findMember(memberId);
    member.withdraw();
  }

  private MemberEntity findMember(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다 : " + memberId));
  }

}
