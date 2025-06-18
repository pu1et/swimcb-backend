package com.project.swimcb.oauth2.adapter.out;

import com.project.swimcb.oauth2.application.port.out.FindMemberPort;
import com.project.swimcb.oauth2.domain.Member;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
class FindMemberInteractor implements FindMemberPort {

  @Override
  public Optional<Member> findByEmail(@NonNull String email) {
    return null;
  }

}
