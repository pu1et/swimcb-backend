package com.project.swimcb.oauth2.adapter.out;

import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.oauth2.application.port.out.SignupPort;
import com.project.swimcb.oauth2.domain.SignupRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class SignupInteractor implements SignupPort {

  private final MemberRepository memberRepository;

  @Override
  public Long signup(@NonNull SignupRequest signupRequest) {
    val member = MemberEntity.builder()
        .email(signupRequest.email())
        .name(signupRequest.name())
        .phone(signupRequest.phoneNumber())
        .nickname("닉네임")
        .build();
    val savedMember = memberRepository.save(member);
    return savedMember.getId();
  }

}
