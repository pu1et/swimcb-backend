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
    @NonNull MemberRole role,
    Long swimmingPoolId
) {

  public static TokenInfo guest() {
    return new TokenInfo(null, GUEST, null);
  }

  public static TokenInfo customer(@NonNull Long memberId) {
    return new TokenInfo(memberId, CUSTOMER, null);
  }

  public static TokenInfo admin(@NonNull Long memberId, @NonNull Long swimmingPoolId) {
    return new TokenInfo(memberId, ADMIN, swimmingPoolId);
  }

  public static TokenInfo fromToken(@NonNull DecodedJWT decodedJWT) {
    val memberId = decodedJWT.getSubject() == null ? null : Long.parseLong(decodedJWT.getSubject());
    val role = MemberRole.valueOf(decodedJWT.getClaim("role").asString());
    val swimmingPoolId = decodedJWT.getClaim("swimmingPoolId") == null ? null
        : decodedJWT.getClaim("swimmingPoolId").asLong();

    return switch (role) {
      case GUEST -> {
        if (memberId != null) {
          throw new IllegalArgumentException("GUEST는 memberId가 존재할 수 없습니다.");
        }
        if (swimmingPoolId != null) {
          throw new IllegalArgumentException("GUEST는 swimmingPoolId가 존재할 수 없습니다.");
        }
        yield guest();
      }
      case CUSTOMER -> {
        if (memberId == null) {
          throw new IllegalArgumentException("CUSTOMER은 memberId가 존재해야 합니다.");
        }
        if (swimmingPoolId != null) {
          throw new IllegalArgumentException("CUSTOMER은 swimmingPoolId가 존재할 수 없습니다.");
        }
        yield customer(memberId);
      }
      case ADMIN -> {
        if (memberId == null) {
          throw new IllegalArgumentException("ADMIN은 memberId가 존재해야 합니다.");
        }
        if (swimmingPoolId == null) {
          throw new IllegalArgumentException("ADMIN은 swimmingPoolId가 존재해야 합니다.");
        }
        yield admin(memberId, swimmingPoolId);
      }
    };
  }
}
