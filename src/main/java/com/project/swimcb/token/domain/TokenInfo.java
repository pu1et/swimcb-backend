package com.project.swimcb.token.domain;

import static com.project.swimcb.token.domain.enums.MemberRole.ADMIN;
import static com.project.swimcb.token.domain.enums.MemberRole.CUSTOMER;
import static com.project.swimcb.token.domain.enums.MemberRole.GUEST;

import com.project.swimcb.token.domain.enums.MemberRole;
import lombok.NonNull;

public record TokenInfo(
    Long memberId,
    @NonNull MemberRole role
) {

  public static TokenInfo guest() {
    return new TokenInfo(null, GUEST);
  }

  public static TokenInfo customer(@NonNull Long memberId) {
    return new TokenInfo(memberId, CUSTOMER);
  }

  public static TokenInfo admin(@NonNull Long memberId) {
    return new TokenInfo(memberId, ADMIN);
  }
}
