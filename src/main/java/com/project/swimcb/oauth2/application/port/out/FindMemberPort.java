package com.project.swimcb.oauth2.application.port.out;

import com.project.swimcb.oauth2.domain.Member;
import java.util.Optional;
import lombok.NonNull;

public interface FindMemberPort {

  Optional<Member> findByEmail(@NonNull String email);
}
