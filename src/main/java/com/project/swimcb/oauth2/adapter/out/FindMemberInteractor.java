package com.project.swimcb.oauth2.adapter.out;

import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.oauth2.application.port.out.FindMemberPort;
import com.project.swimcb.oauth2.domain.Member;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindMemberInteractor implements FindMemberPort {

  private final MemberRepository memberRepository;

  @Override
  public Optional<Member> findByEmail(@NonNull String email) {
    return memberRepository.findByEmail(email)
        .map(member -> Member.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .phoneNumber(member.getPhone())
            .build());
  }

}
