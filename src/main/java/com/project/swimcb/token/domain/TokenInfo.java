package com.project.swimcb.token.domain;

import static com.project.swimcb.token.domain.enums.MemberRole.ADMIN;
import static com.project.swimcb.token.domain.enums.MemberRole.CUSTOMER;
import static com.project.swimcb.token.domain.enums.MemberRole.GUEST;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.swimcb.token.domain.enums.MemberRole;
import lombok.NonNull;
import lombok.val;

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

  public static TokenInfo fromToken(@NonNull DecodedJWT decodedJWT) {
    val role = MemberRole.valueOf(decodedJWT.getClaim("role").asString());
    if ((role == GUEST) != (decodedJWT.getSubject() == null)) {
      throw new IllegalArgumentException("memberId가 존재하면 GUEST일 수 없습니다.");
    }
    if (role == GUEST) {
      return guest();
    }
    return new TokenInfo(Long.parseLong(decodedJWT.getSubject()), role);
  }
}
