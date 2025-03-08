package com.project.swimcb.common;

import com.project.swimcb.token.domain.TokenInfo;
import lombok.NonNull;
import lombok.val;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockTokenInfoSecurityContextFactory implements
    WithSecurityContextFactory<WithMockTokenInfo> {

  @Override
  public SecurityContext createSecurityContext(WithMockTokenInfo annotation) {
    val context = SecurityContextHolder.createEmptyContext();

    val tokenInfo = new TokenInfo(parseLong(annotation.memberId()), annotation.role(),
        annotation.swimmingPoolId());

    val authentication = new TestingAuthenticationToken(tokenInfo, null);
    context.setAuthentication(authentication);

    return context;
  }

  private Long parseLong(@NonNull String value) {
    if (value.isEmpty()) {
      return null;
    }
    return Long.parseLong(value);
  }
}
